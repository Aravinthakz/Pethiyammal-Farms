import { useOutletContext } from 'react-router-dom'
import { waLink } from '../api'

export default function HowToOrder() {
  const steps = [
    ['Choose Animal', 'Browse goats, cows or chicken and open the details page.'],
    ['Send Enquiry', 'Use WhatsApp or the enquiry form with quantity and location.'],
    ['Confirm Availability', 'We confirm the animal, price and pickup or delivery.'],
    ['Advance Payment', 'Pay the agreed advance to reserve the stock.'],
    ['Delivery / Pickup', 'Collect from Namakkal or arrange delivery.'],
  ]
  return (
    <div className="layout-page">
      <div className="container">
        <h1 className="serif" style={{ color: 'var(--green)' }}>How to Order</h1>
        <p className="lede">A simple path from browsing to pickup. No account required.</p>
        <div className="steps">
          {steps.map(([t, d], i) => (
            <div className="step" key={t}>
              <div className="n">{i + 1}</div>
              <strong>{t}</strong>
              <p>{d}</p>
            </div>
          ))}
        </div>
      </div>
    </div>
  )
}

export function About() {
  return (
    <div className="layout-page">
      <div className="container">
        <h1 className="serif" style={{ color: 'var(--green)' }}>About Us</h1>
        <p className="lede">Pethiyammal Farms is a native livestock retailer and wholesaler based in Namakkal, Tamil Nadu.</p>
        <img alt="Pethiyammal Farms logo" src="/pethiyammal-farms-logo.png" style={{ borderRadius: 20, width: '100%', height: 380, objectFit: 'contain', background: '#fff', padding: 24 }} />
        <p>Families, farmers, meat shops, hotels and traders come to us because they can see real photographs, age, weight and price before they call. We keep inventory updated so you spend less time asking whether an animal is still available.</p>
        <div className="stats">
          <div className="stat"><b>5+</b>Years of Experience</div>
          <div className="stat"><b>1000+</b>Happy Customers</div>
          <div className="stat"><b>100%</b>Healthy Animals</div>
          <div className="stat"><b>On-time</b>Delivery</div>
        </div>
      </div>
    </div>
  )
}

export function Contact() {
  const settings = useOutletContext()
  return (
    <div className="layout-page">
      <div className="container">
        <h1 className="serif" style={{ color: 'var(--green)' }}>Get In Touch</h1>
        <div className="contact-grid">
          <div className="form-card" style={{ margin: 0 }}>
            <p><strong>Phone</strong><br />{settings.phone}</p>
            <p><strong>WhatsApp</strong><br />
              <a href={waLink(settings.whatsappNumber, 'Hello Pethiyammal Farms')} target="_blank" rel="noreferrer">Message us</a>
            </p>
            <p><strong>Location</strong><br />{settings.address}</p>
            <p><strong>Hours</strong><br />{settings.hours}</p>
          </div>
          <iframe className="map" title="Namakkal map" src={settings.mapEmbedUrl} />
        </div>
      </div>
    </div>
  )
}
