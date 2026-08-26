import { useState } from 'react'
import { api } from '../api'

export default function Wholesale() {
  const [ok, setOk] = useState('')
  const [err, setErr] = useState('')
  const [form, setForm] = useState({
    businessName: '', contactName: '', phone: '', product: 'Country Chicken',
    quantity: '', budget: '', location: '', requiredDate: '', message: '',
  })
  const set = (k) => (e) => setForm({ ...form, [k]: e.target.value })

  async function submit(e) {
    e.preventDefault()
    setErr('')
    try {
      await api('/api/wholesale', {
        method: 'POST',
        body: JSON.stringify({ ...form, quantity: Number(form.quantity), budget: form.budget ? Number(form.budget) : null }),
      })
      setOk('Wholesale request submitted. Pethiyammal Farms will contact you with a quote.')
    } catch (ex) {
      setErr(ex.message)
    }
  }

  return (
    <div className="layout-page">
      <div className="container">
        <h1 className="serif" style={{ textAlign: 'center', color: 'var(--green)' }}>Wholesale Enquiry</h1>
        <p className="lede" style={{ textAlign: 'center' }}>Bulk livestock supply for hotels, restaurants, meat shops, traders and farms.</p>
        <form className="form-card" onSubmit={submit}>
          {ok && <div className="success">{ok}</div>}
          {err && <div className="success" style={{ background: '#fde8e8', color: 'var(--bad)' }}>{err}</div>}
          <label className="field">Business Name<input required value={form.businessName} onChange={set('businessName')} /></label>
          <label className="field">Contact Person<input required value={form.contactName} onChange={set('contactName')} /></label>
          <label className="field">Phone Number<input required value={form.phone} onChange={set('phone')} /></label>
          <label className="field">Product Interest
            <select value={form.product} onChange={set('product')}>
              <option>Country Chicken</option>
              <option>Goat</option>
              <option>Cow</option>
              <option>Mixed livestock</option>
            </select>
          </label>
          <label className="field">Required Quantity<input required type="number" value={form.quantity} onChange={set('quantity')} /></label>
          <label className="field">Approx Budget<input type="number" value={form.budget} onChange={set('budget')} /></label>
          <label className="field">Delivery Location<input value={form.location} onChange={set('location')} /></label>
          <label className="field">Preferred Date<input type="date" value={form.requiredDate} onChange={set('requiredDate')} /></label>
          <label className="field">Additional Requirements<textarea rows="4" value={form.message} onChange={set('message')} /></label>
          <button className="btn btn-primary btn-block" type="submit">Submit Wholesale Request</button>
        </form>
      </div>
    </div>
  )
}
