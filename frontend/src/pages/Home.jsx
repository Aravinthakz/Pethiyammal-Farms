import { Link, useOutletContext } from 'react-router-dom'
import { useEffect, useState } from 'react'
import { api, waLink } from '../api'
import { ProductCard } from '../Layout'

const HERO = '/form.jpg'
const GOAT = '/goat.jpg'
const COW = '/cow.jpg'
const CHICKEN = '/chicken.jpg'
const ABOUT = '/pethiyammal-farms-logo.png'

export default function Home() {
  const settings = useOutletContext()
  const [featured, setFeatured] = useState([])
  useEffect(() => {
    api('/api/livestock/featured').then(setFeatured).catch(() => {})
  }, [])
  const wa = waLink(settings.whatsappNumber, 'Hello Pethiyammal Farms, I would like to place an order.')

  return (
    <>
      <section className="hero">
        <img src={HERO} alt="Native livestock on Pethiyammal Farms" />
        <div className="hero-copy">
          <div className="kicker">Chicken • Goat • Cow | Retail & Wholesale</div>
          <h1>Quality Native Livestock</h1>
          <p>Healthy animals, fair pricing and direct farm supply from Namakkal.</p>
          <div className="hero-actions">
            <Link className="btn btn-primary" to="/chicken">View Livestock</Link>
            <a className="btn btn-outline" href={wa} target="_blank" rel="noreferrer">Order on WhatsApp</a>
          </div>
        </div>
      </section>

      <div className="container values">
        {[
          ['🌿', 'Quality Livestock'],
          ['🏡', 'Native Breeds'],
          ['⚖', 'Fair Pricing'],
          ['📦', 'Retail & Wholesale'],
          ['☎', 'Customer Support'],
        ].map(([icon, label]) => (
          <div className="value" key={label}>
            <div className="icon">{icon}</div>
            <strong>{label}</strong>
          </div>
        ))}
      </div>

      <section className="section">
        <div className="container">
          <h2>Our Livestock</h2>
          <p className="lede">Browse country chicken, goats and cows with real photographs and current availability.</p>
          <div className="cat-grid">
            <article className="cat-card chicken">
              <img src={CHICKEN} alt="Chicken" />
              <h3>CHICKEN</h3>
              <p>Country chicken</p>
              <Link className="btn btn-primary" to="/chicken">View Chicken</Link>
            </article>
            <article className="cat-card goats">
              <img src={GOAT} alt="Goats" />
              <h3>GOATS</h3>
              <p>Native goats</p>
              <Link className="btn btn-primary" to="/goats">View Goats</Link>
            </article>
            <article className="cat-card cows">
              <img src={COW} alt="Cows" />
              <h3>COWS</h3>
              <p>Healthy cows</p>
              <Link className="btn btn-primary" to="/cows">View Cows</Link>
            </article>
          </div>
        </div>
      </section>

      <section className="section" style={{ paddingTop: 0 }}>
        <div className="container">
          <h2>Featured Livestock</h2>
          <p className="lede">Animals currently available for enquiry.</p>
          <div className="product-grid">
            {featured.map((item) => <ProductCard key={item.id} item={item} settings={settings} />)}
          </div>
        </div>
      </section>

      <section className="section" style={{ paddingTop: 0 }}>
        <div className="container">
          <div className="wholesale-banner">
            <div>
              <h2 style={{ color: '#fff', margin: 0 }}>Wholesale Orders</h2>
              <p>Bulk livestock supply for hotels, restaurants, meat shops, traders and farms.</p>
            </div>
            <Link className="btn" style={{ background: '#fff', color: 'var(--green)' }} to="/wholesale">Request Wholesale Quote</Link>
          </div>
        </div>
      </section>

      <section className="section" style={{ paddingTop: 0 }}>
        <div className="container">
          <h2>How to Order</h2>
          <div className="steps">
            {['Choose Animal', 'Send Enquiry', 'Confirm Details', 'Advance Payment', 'Delivery / Pickup'].map((s, i) => (
              <div className="step" key={s}><div className="n">{i + 1}</div><strong>{s}</strong></div>
            ))}
          </div>
          <Link className="btn btn-primary" style={{ marginTop: 20 }} to="/enquiry">Order Now</Link>
          <p style={{ marginTop: 18 }}><Link to="/how-to-order">See the full ordering process →</Link></p>
        </div>
      </section>

      <section className="section" style={{ paddingTop: 0 }}>
        <div className="container" style={{ display: 'grid', gridTemplateColumns: '1.1fr .9fr', gap: 28, alignItems: 'center' }}>
          <img src={ABOUT} alt="Pethiyammal Farms" style={{ borderRadius: 20, width: '100%', height: 320, objectFit: 'contain', background: '#fff', padding: 20 }} />
          <div>
            <h2>About Pethiyammal Farms</h2>
            <p>We supply native livestock from Namakkal with clear photographs, weight, age and honest pricing — so you can enquire with confidence.</p>
            <Link className="btn btn-primary" to="/about">Our Story</Link>
          </div>
        </div>
      </section>

      <div className="info-bar">
        <div className="container">
          <div><strong>Call / WhatsApp</strong>{settings.phone}</div>
          <div><strong>Location</strong>{settings.address}</div>
          <div><strong>Working Hours</strong>{settings.hours}</div>
        </div>
      </div>
    </>
  )
}
