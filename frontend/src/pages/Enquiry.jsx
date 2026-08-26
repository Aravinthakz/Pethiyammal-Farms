import { useEffect, useState } from 'react'
import { useOutletContext, useSearchParams } from 'react-router-dom'
import { api, getEnquiryList, livestockWhatsAppText, waLink } from '../api'

export default function Enquiry() {
  const settings = useOutletContext()
  const [params] = useSearchParams()
  const [livestock, setLivestock] = useState([])
  const [ok, setOk] = useState('')
  const [err, setErr] = useState('')
  const [form, setForm] = useState({
    fullName: '',
    phone: '',
    livestockId: params.get('livestockId') || '',
    quantity: params.get('qty') || 1,
    preferredDate: '',
    deliveryLocation: '',
    message: '',
  })

  useEffect(() => {
    api('/api/livestock').then(setLivestock).catch(() => {})
  }, [])

  const selected = livestock.find((l) => String(l.id) === String(form.livestockId))
  const set = (k) => (e) => setForm({ ...form, [k]: e.target.value })

  async function submit(e) {
    e.preventDefault()
    setErr('')
    try {
      await api('/api/enquiries', {
        method: 'POST',
        body: JSON.stringify({
          ...form,
          livestockId: form.livestockId ? Number(form.livestockId) : null,
          quantity: Number(form.quantity),
          productSelection: selected ? `${selected.title} (${selected.animalCode})` : form.livestockId,
        }),
      })
      setOk('Enquiry sent. We will contact you shortly.')
    } catch (ex) {
      setErr(ex.message)
    }
  }

  const saved = getEnquiryList()

  return (
    <div className="layout-page">
      <div className="container">
        <h1 className="serif" style={{ textAlign: 'center', color: 'var(--green)' }}>Enquiry / Order</h1>
        <form className="form-card" onSubmit={submit}>
          {ok && <div className="success enquiry-alert" role="alert">{ok}</div>}
          {err && <div className="success enquiry-alert enquiry-error" role="alert">{err}</div>}
          <label className="field">Full Name<input required value={form.fullName} onChange={set('fullName')} /></label>
          <label className="field">Phone Number<input required value={form.phone} onChange={set('phone')} /></label>
          <label className="field">Product Selection
            <select value={form.livestockId} onChange={set('livestockId')}>
              <option value="">Select an animal</option>
              {livestock.map((l) => <option key={l.id} value={l.id}>{l.title} — {l.animalCode}</option>)}
            </select>
          </label>
          <label className="field">Quantity<input type="number" min="1" value={form.quantity} onChange={set('quantity')} /></label>
          <label className="field">Preferred Date<input type="date" value={form.preferredDate} onChange={set('preferredDate')} /></label>
          <label className="field">Delivery Location<input value={form.deliveryLocation} onChange={set('deliveryLocation')} /></label>
          <label className="field">Message<textarea rows="4" value={form.message} onChange={set('message')} /></label>
          <button className="btn btn-primary btn-block" type="submit">Send Enquiry</button>
          {selected && (
            <a className="btn btn-wa btn-block" style={{ marginTop: 10 }} target="_blank" rel="noreferrer"
              href={waLink(settings.whatsappNumber, livestockWhatsAppText(selected, `Quantity: ${form.quantity}`))}>
              Send Enquiry on WhatsApp
            </a>
          )}
        </form>
        {saved.length > 0 && (
          <p className="container" style={{ marginTop: 20, textAlign: 'center' }}>
            Saved list: {saved.map((s) => s.title).join(', ')}
          </p>
        )}
      </div>
    </div>
  )
}
