import { Link, useNavigate, useOutletContext, useParams } from 'react-router-dom'
import { useEffect, useMemo, useState } from 'react'
import { api, defaultLivestockImage, inEnquiryList, livestockWhatsAppText, toggleEnquiryList, waLink } from '../api'
import { ProductCard } from '../Layout'

export default function Details() {
  const { id } = useParams()
  const settings = useOutletContext()
  const navigate = useNavigate()
  const [item, setItem] = useState(null)
  const [similar, setSimilar] = useState([])
  const [qty, setQty] = useState(5)
  const [saved, setSaved] = useState(false)
  const [photo, setPhoto] = useState(0)

  useEffect(() => {
    api(`/api/livestock/${id}`).then((data) => {
      setItem(data)
      setPhoto(0)
      setQty(data.minOrderQty || 1)
      setSaved(inEnquiryList(data.id))
    }).catch(() => setItem(null))
    api(`/api/livestock/${id}/similar`)
      .then((data) => setSimilar(Array.isArray(data) ? data : []))
      .catch(() => setSimilar([]))
  }, [id])

  const total = useMemo(() => {
    if (!item) return 0
    return item.pricingType === 'PER_KG' ? Number(item.price) * Number(qty || 0) : Number(item.price)
  }, [item, qty])

  if (!item) return <div className="layout-page container">Loading…</div>
  const unavailable = item.status !== 'AVAILABLE'
  const extra = item.pricingType === 'PER_KG' ? `Quantity: ${qty} KG\nTotal: ₹${total}` : ''
  const wa = waLink(settings.whatsappNumber, livestockWhatsAppText(item, extra))
  const img = item.images?.[photo]?.imageUrl || item.images?.[0]?.imageUrl || defaultLivestockImage(item.category)

  return (
    <div className="layout-page">
      <div className="container details">
        <div>
          <div className="gallery">
            {img && <img className="main" src={img} alt={item.title} onError={(e) => { e.currentTarget.onerror = null; e.currentTarget.src = defaultLivestockImage(item.category) }} />}
            <div className="thumbs">
              {item.images?.map((im, i) => (
                <img key={im.id} src={im.imageUrl} alt={`${item.title} photo ${i + 1}`} onClick={() => setPhoto(i)} onError={(e) => { e.currentTarget.onerror = null; e.currentTarget.src = defaultLivestockImage(item.category) }} />
              ))}
            </div>
          </div>
        </div>
        <div className="detail-panel">
          <span className={`badge ${item.status.toLowerCase()}`} style={{ position: 'static' }}>{item.status === 'AVAILABLE' ? 'Available' : item.status}</span>
          <h1 className="serif" style={{ marginBottom: 4 }}>{item.title}</h1>
          <div className="price">
            {item.pricingType === 'PER_KG' ? `₹${item.price} / KG` : `₹${Number(item.price).toLocaleString('en-IN')}`}
          </div>
          <table className="spec">
            <tbody>
              <tr><td>Animal ID</td><td>{item.animalCode}</td></tr>
              <tr><td>Breed</td><td>{item.breed}</td></tr>
              {item.gender && <tr><td>Gender</td><td>{item.gender}</td></tr>}
              <tr><td>Age</td><td>{item.ageLabel}</td></tr>
              <tr><td>Weight</td><td>{item.weightKg} KG</td></tr>
              <tr><td>Location</td><td>{item.location}</td></tr>
              {item.pricingType === 'PER_KG' && (
                <>
                  <tr><td>Minimum order</td><td>{item.minOrderQty} KG</td></tr>
                  <tr><td>Available quantity</td><td>{item.availableQty} KG</td></tr>
                </>
              )}
            </tbody>
          </table>
          {item.pricingType === 'PER_KG' && (
            <div className="qty-row">
              <label>Quantity (KG)</label>
              <input type="number" min={item.minOrderQty || 1} max={item.availableQty || undefined} value={qty} onChange={(e) => setQty(e.target.value)} />
              <strong>Total ₹{total.toLocaleString('en-IN')}</strong>
            </div>
          )}
          {unavailable ? (
            <p>This animal is no longer available for new orders.</p>
          ) : (
            <>
              <a className="btn btn-primary btn-block" href={wa} target="_blank" rel="noreferrer">Enquire on WhatsApp</a>
              <a className="btn btn-ghost btn-block" style={{ marginTop: 8 }} href={`tel:${settings.phone}`}>Call Now</a>
              <button
                className="btn btn-ghost btn-block"
                style={{ marginTop: 8 }}
                onClick={() => setSaved(toggleEnquiryList(item).some((x) => x.id === item.id))}
              >
                {saved ? 'Saved in enquiry list' : 'Add to Enquiry List'}
              </button>
              <button
                className="btn btn-primary btn-block"
                style={{ marginTop: 8 }}
                onClick={() => navigate(`/enquiry?livestockId=${item.id}&qty=${qty}`)}
              >
                Order Now
              </button>
            </>
          )}
        </div>
      </div>
      <div className="container" style={{ marginTop: 32 }}>
        <h2>About This {item.category === 'CHICKEN' ? 'Chicken' : item.category === 'COW' ? 'Cow' : 'Goat'}</h2>
        <p>{item.description}</p>
        <h3>Why Choose This Animal?</h3>
        <p>{item.whyChoose}</p>
        <h2>Similar Livestock</h2>
        <div className="product-grid">
          {similar.map((s) => <ProductCard key={s.id} item={s} settings={settings} />)}
        </div>
        <p><Link to={item.category === 'CHICKEN' ? '/chicken' : item.category === 'COW' ? '/cows' : '/goats'}>Back to listing</Link></p>
      </div>
    </div>
  )
}
