import { NavLink, Outlet, Link } from 'react-router-dom'
import { useEffect, useState } from 'react'
import { api, defaultLivestockImage, waLink } from './api'

export function useSettings() {
  const [settings, setSettings] = useState({
    whatsappNumber: '919876543210',
    phone: '9876543210',
    address: 'Namakkal, Tamil Nadu',
    hours: 'Mon–Sat, 8:00 AM – 7:00 PM',
    mapEmbedUrl: 'https://maps.google.com/maps?q=Namakkal&t=&z=13&ie=UTF8&iwloc=&output=embed',
  })
  useEffect(() => {
    api('/api/settings/public').then(setSettings).catch(() => {})
  }, [])
  return settings
}

export function Layout() {
  const settings = useSettings()
  const [open, setOpen] = useState(false)
  const wa = waLink(settings.whatsappNumber, 'Hello Pethiyammal Farms, I would like to know more about your animals.')

  return (
    <>
      <header className="header">
        <div className="header-inner">
          <div className="brand-group">
            <Link to="/" className="logo">
              <img className="brand-logo" src="/pethiyammal-farms-logo.png" alt="Pethiyammal Farms" />
            </Link>
          </div>
          <button className="menu-btn" onClick={() => setOpen(!open)} aria-label="Menu">☰</button>
          <nav className={`nav ${open ? 'open' : ''}`} onClick={() => setOpen(false)}>
            <NavLink to="/" end>Home</NavLink>
            <NavLink to="/chicken">Chicken</NavLink>
            <NavLink to="/goats">Goats</NavLink>
            <NavLink to="/cows">Cows</NavLink>
            <NavLink to="/wholesale">Wholesale</NavLink>
            <NavLink to="/about">About</NavLink>
            <NavLink to="/contact">Contact</NavLink>
          </nav>
          <a className="btn btn-primary btn-sm" href={wa} target="_blank" rel="noreferrer">WhatsApp Us</a>
        </div>
      </header>
      <Outlet context={settings} />
      <footer className="footer">
        <div className="container footer-grid">
          <div>
            <div className="logo" style={{ color: '#fff', marginBottom: 12 }}>
              <img className="brand-logo footer-logo" src="/pethiyammal-farms-logo.png" alt="Pethiyammal Farms" />
            </div>
            <p>Digital livestock marketplace and direct farm supply from Namakkal. Native chicken, goats and cows for retail and wholesale.</p>
          </div>
          <div>
            <h4>Quick Links</h4>
            <Link to="/">Home</Link>
            <Link to="/chicken">Chicken</Link>
            <Link to="/goats">Goats</Link>
            <Link to="/cows">Cows</Link>
            <Link to="/wholesale">Wholesale</Link>
            <Link to="/how-to-order">How to Order</Link>
            <Link to="/about">About Us</Link>
            <Link to="/contact">Contact</Link>
          </div>
          <div>
            <h4>Support</h4>
            <Link to="/privacy">Privacy Policy</Link>
            <Link to="/terms">Terms & Conditions</Link>
            <Link to="/shipping">Shipping Policy</Link>
            <Link to="/refund">Refund Policy</Link>
            <Link to="/faq">FAQ</Link>
          </div>
        </div>
        <div className="container copyright">© {new Date().getFullYear()} PETHIYAMMAL FARMS. All Rights Reserved.</div>
      </footer>
      <a className="fab-wa" href={wa} target="_blank" rel="noreferrer" aria-label="WhatsApp">💬</a>
      <nav className="mobile-nav">
        <Link to="/">Home</Link>
        <Link to="/chicken">Livestock</Link>
        <Link to="/enquiry">Enquiry</Link>
        <Link to="/contact">More</Link>
      </nav>
    </>
  )
}

export function ProductCard({ item, settings }) {
  const img = item.images?.[0]?.imageUrl || defaultLivestockImage(item.category)
  const wa = waLink(settings?.whatsappNumber, `Hello Pethiyammal Farms,\n\nI am interested in:\n\nProduct: ${item.title}\nAnimal ID: ${item.animalCode}\nWeight: ${item.weightKg || '-'} KG\nPrice: ₹${item.price}${item.pricingType === 'PER_KG' ? ' / KG' : ''}\n\nPlease confirm availability.`)
  return (
    <article className="product-card">
      <div className="media">
        {img && <img src={img} alt={item.title} onError={(e) => { e.currentTarget.onerror = null; e.currentTarget.src = defaultLivestockImage(item.category) }} />}
        <span className={`badge ${item.status?.toLowerCase()}`}>{item.status === 'AVAILABLE' ? 'Available' : item.status}</span>
      </div>
      <div className="body">
        <h3>{item.title}</h3>
        <div className="meta">
          {item.ageLabel && <span>{item.ageLabel}</span>}
          {item.weightKg && <span>{item.weightKg} KG</span>}
        </div>
        <div className="price">
          {item.pricingType === 'PER_KG' ? `₹${item.price} / KG` : `₹${Number(item.price).toLocaleString('en-IN')}`}
        </div>
        <div className="card-actions">
          <Link className="btn btn-ghost btn-sm" to={`/livestock/${item.id}`}>View Details</Link>
          <a className="btn btn-wa btn-sm" href={wa} target="_blank" rel="noreferrer">Enquire on WhatsApp</a>
        </div>
      </div>
    </article>
  )
}

export function PolicyPage({ title, children }) {
  return (
    <div className="layout-page">
      <div className="container form-card">
        <h1 className="serif">{title}</h1>
        {children}
      </div>
    </div>
  )
}
