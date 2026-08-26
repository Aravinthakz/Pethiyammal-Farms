import { useEffect, useState } from 'react'
import { useOutletContext } from 'react-router-dom'
import { api } from '../api'
import { ProductCard } from '../Layout'

const META = {
  CHICKEN: { title: 'Chicken', lede: 'Country chicken sold by weight for homes, hotels and meat shops.', seo: 'Country Chicken for Sale | Pethiyammal Farms' },
  GOAT: { title: 'Goats', lede: 'Healthy native goats available for your farm and home.', seo: 'Native Goats for Sale in Namakkal | Pethiyammal Farms' },
  COW: { title: 'Cows', lede: 'Healthy cows from Pethiyammal Farms.', seo: 'Native Cows for Sale | Pethiyammal Farms' },
}

export default function Listing({ category }) {
  const settings = useOutletContext()
  const meta = META[category]
  const [items, setItems] = useState([])
  const [age, setAge] = useState('')
  const [gender, setGender] = useState('')
  const [price, setPrice] = useState('')
  const [sort, setSort] = useState('')

  useEffect(() => {
    document.title = meta.seo
    const q = new URLSearchParams()
    q.set('category', category)
    if (age) q.set('age', age)
    if (gender) q.set('gender', gender)
    if (price) q.set('price', price)
    if (sort) q.set('sort', sort)
    api(`/api/livestock?${q}`)
      .then((data) => setItems(Array.isArray(data) ? data : []))
      .catch(() => setItems([]))
  }, [category, age, gender, price, sort, meta.seo])

  return (
    <div className="layout-page">
      <div className="container">
        <h1 className="serif" style={{ color: 'var(--green)' }}>{meta.title}</h1>
        <p className="lede">{meta.lede}</p>
        <div className="listing">
          <aside className="filters">
            <h3>Filter</h3>
            {category !== 'CHICKEN' && (
              <>
                <h4>Age</h4>
                {[['0-6', '0–6 Months'], ['6-12', '6–12 Months'], ['12-24', '1–2 Years'], ['24+', '2+ Years']].map(([v, l]) => (
                  <label key={v}><input type="radio" name="age" checked={age === v} onChange={() => setAge(v)} /> {l}</label>
                ))}
                <label><input type="radio" name="age" checked={age === ''} onChange={() => setAge('')} /> Any</label>
                <h4>Gender</h4>
                <label><input type="radio" name="g" checked={gender === 'MALE'} onChange={() => setGender('MALE')} /> Male</label>
                <label><input type="radio" name="g" checked={gender === 'FEMALE'} onChange={() => setGender('FEMALE')} /> Female</label>
                <label><input type="radio" name="g" checked={gender === ''} onChange={() => setGender('')} /> Any</label>
              </>
            )}
            <h4>Price</h4>
            {[['under-10000', 'Under ₹10,000'], ['10000-25000', '₹10,000–₹25,000'], ['25000-50000', '₹25,000–₹50,000'], ['50000+', 'Above ₹50,000']].map(([v, l]) => (
              <label key={v}><input type="radio" name="p" checked={price === v} onChange={() => setPrice(v)} /> {l}</label>
            ))}
            <label><input type="radio" name="p" checked={price === ''} onChange={() => setPrice('')} /> Any</label>
          </aside>
          <div>
            <div className="toolbar">
              <span>{items.length} animals</span>
              <select value={sort} onChange={(e) => setSort(e.target.value)}>
                <option value="">Sort by latest</option>
                <option value="price-asc">Price: Low to High</option>
                <option value="price-desc">Price: High to Low</option>
              </select>
            </div>
            <div className="product-grid">
              {items.map((item) => <ProductCard key={item.id} item={item} settings={settings} />)}
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}
