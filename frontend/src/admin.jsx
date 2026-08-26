import { useEffect, useState } from 'react'
import { Link, Navigate, NavLink, Outlet, useNavigate } from 'react-router-dom'
import { api } from './api'

export function AdminLogin() {
  const nav = useNavigate()
  const [username, setUsername] = useState('admin')
  const [password, setPassword] = useState('admin123')
  const [err, setErr] = useState('')
  async function submit(e) {
    e.preventDefault()
    try {
      const res = await api('/api/auth/login', { method: 'POST', body: JSON.stringify({ username, password }) })
      localStorage.setItem('rmsvg-token', res.token)
      localStorage.setItem('rmsvg-admin', JSON.stringify(res))
      nav('/admin')
    } catch (ex) {
      setErr(ex.message)
    }
  }
  if (localStorage.getItem('rmsvg-token')) return <Navigate to="/admin" replace />
  return (
    <div className="login-wrap">
      <form className="login-card" onSubmit={submit}>
        <h1 className="serif" style={{ color: 'var(--green)' }}>Pethiyammal Farms Admin</h1>
        {err && <p style={{ color: 'var(--bad)' }}>{err}</p>}
        <label className="field">Username<input value={username} onChange={(e) => setUsername(e.target.value)} /></label>
        <label className="field">Password<input type="password" value={password} onChange={(e) => setPassword(e.target.value)} /></label>
        <button className="btn btn-primary btn-block">Login</button>
      </form>
    </div>
  )
}

export function AdminShell() {
  if (!localStorage.getItem('rmsvg-token')) return <Navigate to="/admin/login" replace />
  const logout = () => {
    localStorage.removeItem('rmsvg-token')
    window.location.href = '/admin/login'
  }
  return (
    <div className="admin-shell">
      <aside className="admin-side">
        <Link to="/" style={{ color: '#fff', display: 'block', marginBottom: 16, fontWeight: 700 }}>PETHIYAMMAL FARMS ADMIN</Link>
        <NavLink to="/admin" end>Dashboard</NavLink>
        <NavLink to="/admin/livestock">Livestock</NavLink>
        <NavLink to="/admin/livestock/new">Add Livestock</NavLink>
        <NavLink to="/admin/orders">Orders</NavLink>
        <NavLink to="/admin/enquiries">Enquiries</NavLink>
        <NavLink to="/admin/wholesale">Wholesale</NavLink>
        <NavLink to="/admin/customers">Customers</NavLink>
        <NavLink to="/admin/reports">Reports</NavLink>
        <NavLink to="/admin/settings">Settings</NavLink>
        <button onClick={logout}>Logout</button>
      </aside>
      <main className="admin-main">
        <Outlet />
      </main>
    </div>
  )
}

export function Dashboard() {
  const [data, setData] = useState(null)
  useEffect(() => { api('/api/admin/dashboard').then(setData) }, [])
  if (!data) return <p>Loading…</p>
  return (
    <>
      <h1 className="serif">Dashboard</h1>
      <div className="kpis">
        <div className="kpi"><b>{data.totalLivestock}</b>Total Livestock</div>
        <div className="kpi"><b>{data.available}</b>Available</div>
        <div className="kpi"><b>{data.sold}</b>Sold</div>
        <div className="kpi"><b>{data.pendingOrders}</b>Pending Orders</div>
        <div className="kpi"><b>{data.wholesaleRequests}</b>Wholesale Requests</div>
        <div className="kpi"><b>{data.newEnquiries}</b>New Enquiries</div>
      </div>
      <h3>Recent Orders</h3>
      <table className="table">
        <thead><tr><th>ID</th><th>Customer</th><th>Product</th><th>Amount</th><th>Status</th></tr></thead>
        <tbody>
          {data.recentOrders?.map((o) => (
            <tr key={o.id}><td>#{o.id}</td><td>{o.customerName}</td><td>{o.product}</td><td>₹{o.totalAmount}</td><td>{o.orderStatus}</td></tr>
          ))}
        </tbody>
      </table>
    </>
  )
}

const emptyForm = {
  animalCode: '', category: 'CHICKEN', breed: '', gender: 'MALE', ageLabel: '', ageMonths: '',
  weightKg: '', pricingType: 'FIXED', price: '', minOrderQty: '', availableQty: '',
  location: 'Namakkal', description: '', whyChoose: '', status: 'AVAILABLE', featured: false,
}

function AdminNotice({ message }) {
  return message && <div className="admin-notice" role="status">{message}</div>
}

export function LivestockAdmin() {
  const [rows, setRows] = useState([])
  const [category, setCategory] = useState('')
  const [status, setStatus] = useState('')
  const [notice, setNotice] = useState('')
  useEffect(() => {
    const savedNotice = sessionStorage.getItem('livestock-notice')
    if (!savedNotice) return
    sessionStorage.removeItem('livestock-notice')
    setNotice(savedNotice)
    const timer = setTimeout(() => setNotice(''), 3000)
    return () => clearTimeout(timer)
  }, [])
  const showNotice = (message) => {
    setNotice(message)
    setTimeout(() => setNotice(''), 3000)
  }
  const load = () => {
    const q = new URLSearchParams()
    if (category) q.set('category', category)
    if (status) q.set('status', status)
    api(`/api/admin/livestock?${q}`).then(setRows)
  }
  useEffect(() => { load() }, [category, status])

  async function changeStatus(id, next) {
    await api(`/api/admin/livestock/${id}/status`, { method: 'PATCH', body: JSON.stringify({ status: next }) })
    load()
    showNotice('Livestock status updated.')
  }
  async function changePrice(id) {
    const price = prompt('New price')
    if (!price) return
    await api(`/api/admin/livestock/${id}/price`, { method: 'PATCH', body: JSON.stringify({ price: Number(price) }) })
    load()
    showNotice('Livestock price updated.')
  }
  async function remove(id, code) {
    if (!confirm(`Delete livestock ${code}? This hides it from the store (soft delete).`)) return
    await api(`/api/admin/livestock/${id}`, { method: 'DELETE' })
    load()
    showNotice('Livestock deleted.')
  }

  return (
    <>
      <AdminNotice message={notice} />
      <div className="toolbar">
        <h1 className="serif">Livestock</h1>
        <Link className="btn btn-primary" to="/admin/livestock/new">+ Add Livestock</Link>
      </div>
      <div style={{ display: 'flex', gap: 8, marginBottom: 12 }}>
        <select value={category} onChange={(e) => setCategory(e.target.value)}>
          <option value="">All categories</option>
          <option>CHICKEN</option><option>GOAT</option><option>COW</option>
        </select>
        <select value={status} onChange={(e) => setStatus(e.target.value)}>
          <option value="">All statuses</option>
          <option>AVAILABLE</option><option>RESERVED</option><option>SOLD</option>
        </select>
      </div>
      <table className="table">
        <thead><tr><th>ID</th><th>Animal</th><th>Category</th><th>Weight</th><th>Price</th><th>Status</th><th></th></tr></thead>
        <tbody>
          {rows.map((r) => (
            <tr key={r.id}>
              <td>{r.animalCode}</td>
              <td>{r.title}</td>
              <td>{r.category}</td>
              <td>{r.weightKg} KG</td>
              <td>₹{r.price}{r.pricingType === 'PER_KG' ? '/KG' : ''}</td>
              <td>{r.status}</td>
              <td>
                <Link to={`/admin/livestock/${r.id}`}>Edit</Link>{' '}
                <button onClick={() => changePrice(r.id)}>Price</button>{' '}
                <select value={r.status} onChange={(e) => changeStatus(r.id, e.target.value)}>
                  <option>AVAILABLE</option><option>RESERVED</option><option>SOLD</option>
                </select>{' '}
                <button onClick={() => remove(r.id, r.animalCode)}>Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </>
  )
}

export function LivestockForm({ id }) {
  const nav = useNavigate()
  const [form, setForm] = useState(emptyForm)
  const [images, setImages] = useState([])
  const [selectedFile, setSelectedFile] = useState(null)
  const [notice, setNotice] = useState('')
  useEffect(() => {
    const savedNotice = sessionStorage.getItem('livestock-notice')
    if (!savedNotice) return
    sessionStorage.removeItem('livestock-notice')
    setNotice(savedNotice)
    const timer = setTimeout(() => setNotice(''), 3000)
    return () => clearTimeout(timer)
  }, [])
  const showNotice = (message) => {
    setNotice(message)
    setTimeout(() => setNotice(''), 3000)
  }
  const set = (k) => (e) => {
    const v = e.target.type === 'checkbox' ? e.target.checked : e.target.value
    setForm({ ...form, [k]: v })
  }
  useEffect(() => {
    if (!id) return
    api(`/api/admin/livestock/${id}`).then((d) => {
      setForm({
        animalCode: d.animalCode, category: d.category, breed: d.breed, gender: d.gender || '',
        ageLabel: d.ageLabel || '', ageMonths: d.ageMonths || '', weightKg: d.weightKg || '',
        pricingType: d.pricingType, price: d.price, minOrderQty: d.minOrderQty || '',
        availableQty: d.availableQty || '', location: d.location || '', description: d.description || '',
        whyChoose: d.whyChoose || '', status: d.status, featured: d.featured,
      })
      setImages(d.images || [])
    })
  }, [id])

  async function save(e) {
    e.preventDefault()
    const body = {
      ...form,
      ageMonths: form.ageMonths ? Number(form.ageMonths) : null,
      weightKg: form.weightKg ? Number(form.weightKg) : null,
      price: Number(form.price),
      minOrderQty: form.minOrderQty ? Number(form.minOrderQty) : null,
      availableQty: form.availableQty ? Number(form.availableQty) : null,
      gender: form.gender || null,
    }
    const saved = id
      ? await api(`/api/admin/livestock/${id}`, { method: 'PUT', body: JSON.stringify(body) })
      : await api('/api/admin/livestock', { method: 'POST', body: JSON.stringify(body) })
    if (selectedFile) {
      const fd = new FormData()
      fd.append('file', selectedFile)
      const updated = await api(`/api/admin/livestock/${saved.id}/images`, { method: 'POST', body: fd })
      setImages(updated.images)
    }
    const message = id ? 'Livestock details updated.' : 'Livestock added successfully.'
    if (id) showNotice(message)
    else sessionStorage.setItem('livestock-notice', message)
    nav(`/admin/livestock/${saved.id}`)
  }

  async function upload(e) {
    const file = e.target.files[0]
    if (!file || !id) return
    const fd = new FormData()
    fd.append('file', file)
    const d = await api(`/api/admin/livestock/${id}/images`, { method: 'POST', body: fd })
    setImages(d.images)
    showNotice('Livestock photo updated.')
  }

  async function delImg(imageId) {
    const d = await api(`/api/admin/livestock/${id}/images/${imageId}`, { method: 'DELETE' })
    setImages(d.images)
    showNotice('Livestock photo deleted.')
  }

  return (
    <form className="form-card" style={{ maxWidth: 720, margin: 0 }} onSubmit={save}>
      <AdminNotice message={notice} />
      <h1 className="serif">{id ? 'Edit Livestock' : 'Add Livestock'}</h1>
      <label className="field">Animal ID<input required value={form.animalCode} onChange={set('animalCode')} /></label>
      <label className="field">Category
        <select value={form.category} onChange={set('category')}><option>CHICKEN</option><option>GOAT</option><option>COW</option></select>
      </label>
      <label className="field">Breed<input required value={form.breed} onChange={set('breed')} /></label>
      <label className="field">Gender
        <select value={form.gender} onChange={set('gender')}><option value="">—</option><option>MALE</option><option>FEMALE</option></select>
      </label>
      <label className="field">Age<input value={form.ageLabel} onChange={set('ageLabel')} placeholder="8 Months" /></label>
      <label className="field">Age (months)<input type="number" value={form.ageMonths} onChange={set('ageMonths')} /></label>
      <label className="field">Weight (KG)<input type="number" value={form.weightKg} onChange={set('weightKg')} /></label>
      <label className="field">Pricing
        <select value={form.pricingType} onChange={set('pricingType')}><option>FIXED</option><option>PER_KG</option></select>
      </label>
      <label className="field">Price<input required type="number" value={form.price} onChange={set('price')} /></label>
      {form.pricingType === 'PER_KG' && (
        <>
          <label className="field">Min order qty<input type="number" value={form.minOrderQty} onChange={set('minOrderQty')} /></label>
          <label className="field">Available qty<input type="number" value={form.availableQty} onChange={set('availableQty')} /></label>
        </>
      )}
      <label className="field">Location<input value={form.location} onChange={set('location')} /></label>
      <label className="field">Description<textarea rows="3" value={form.description} onChange={set('description')} /></label>
      <label className="field">Why choose<textarea rows="3" value={form.whyChoose} onChange={set('whyChoose')} /></label>
      <label className="field">Status
        <select value={form.status} onChange={set('status')}><option>AVAILABLE</option><option>RESERVED</option><option>SOLD</option></select>
      </label>
      <label className="field" style={{ flexDirection: 'row', alignItems: 'center' }}>
        <input type="checkbox" checked={form.featured} onChange={set('featured')} /> Featured
      </label>
      <button className="btn btn-primary">Save Livestock</button>
      <div style={{ marginTop: 24 }}>
        <h3>Photo</h3>
        {id && (
          <div className="thumbs">
            {images.map((im) => (
              <div key={im.id}>
                <img src={im.imageUrl} alt="" />
                <button type="button" onClick={() => delImg(im.id)}>Delete</button>
              </div>
            ))}
          </div>
        )}
        <input type="file" accept="image/*" onChange={id ? upload : (e) => setSelectedFile(e.target.files[0] || null)} />
        {!id && selectedFile && <span>{selectedFile.name}</span>}
      </div>
    </form>
  )
}

function StatusTable({ path, statusKey, options, columns }) {
  const [rows, setRows] = useState([])
  const load = () => api(path).then(setRows)
  useEffect(() => { load() }, [path])
  async function upd(id, status) {
    const method = path.includes('orders') ? 'PUT' : 'PATCH'
    const url = path.includes('orders') ? `${path}/${id}/status` : `${path}/${id}`
    await api(url, { method, body: JSON.stringify({ status }) })
    load()
  }
  return (
    <table className="table">
      <thead><tr>{columns.map((c) => <th key={c}>{c}</th>)}</tr></thead>
      <tbody>
        {rows.map((r) => (
          <tr key={r.id}>
            {columns.slice(0, -1).map((c) => <td key={c}>{String(r[statusKey[c]] ?? '')}</td>)}
            <td>
              <select value={r[statusKey.Status]} onChange={(e) => upd(r.id, e.target.value)}>
                {options.map((o) => <option key={o}>{o}</option>)}
              </select>
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  )
}

export function EnquiriesAdmin() {
  return (
    <>
      <h1 className="serif">Enquiries</h1>
      <StatusTable
        path="/api/admin/enquiries"
        options={['NEW', 'CONTACTED', 'INTERESTED', 'CONFIRMED', 'NOT_INTERESTED', 'CLOSED']}
        columns={['ID', 'Customer', 'Phone', 'Product', 'Status']}
        statusKey={{ ID: 'id', Customer: 'customerName', Phone: 'phone', Product: 'product', Status: 'status' }}
      />
    </>
  )
}

export function WholesaleAdmin() {
  return (
    <>
      <h1 className="serif">Wholesale Requests</h1>
      <StatusTable
        path="/api/admin/wholesale"
        options={['NEW', 'CONTACTED', 'QUOTED', 'CONFIRMED', 'CLOSED']}
        columns={['ID', 'Business', 'Product', 'Qty', 'Status']}
        statusKey={{ ID: 'id', Business: 'businessName', Product: 'product', Qty: 'quantity', Status: 'status' }}
      />
    </>
  )
}

export function OrdersAdmin() {
  const [form, setForm] = useState({ customerName: '', phone: '', deliveryAddress: '', livestockId: '', quantity: 1 })
  const [stock, setStock] = useState([])
  useEffect(() => { api('/api/admin/livestock').then(setStock) }, [])
  async function create(e) {
    e.preventDefault()
    await api('/api/admin/orders', { method: 'POST', body: JSON.stringify({ ...form, livestockId: Number(form.livestockId), quantity: Number(form.quantity) }) })
    window.location.reload()
  }
  return (
    <>
      <h1 className="serif">Orders</h1>
      <form onSubmit={create} style={{ display: 'flex', gap: 8, flexWrap: 'wrap', marginBottom: 16 }}>
        <input placeholder="Customer" required value={form.customerName} onChange={(e) => setForm({ ...form, customerName: e.target.value })} />
        <input placeholder="Phone" required value={form.phone} onChange={(e) => setForm({ ...form, phone: e.target.value })} />
        <select required value={form.livestockId} onChange={(e) => setForm({ ...form, livestockId: e.target.value })}>
          <option value="">Product</option>
          {stock.map((s) => <option key={s.id} value={s.id}>{s.animalCode}</option>)}
        </select>
        <input type="number" style={{ width: 80 }} value={form.quantity} onChange={(e) => setForm({ ...form, quantity: e.target.value })} />
        <button className="btn btn-primary btn-sm">Create order</button>
      </form>
      <StatusTable
        path="/api/admin/orders"
        options={['PENDING', 'CONTACTED', 'CONFIRMED', 'PAYMENT_RECEIVED', 'READY', 'DELIVERED', 'COMPLETED', 'CANCELLED']}
        columns={['ID', 'Customer', 'Amount', 'Status']}
        statusKey={{ ID: 'id', Customer: 'customerName', Amount: 'totalAmount', Status: 'orderStatus' }}
      />
    </>
  )
}

export function CustomersAdmin() {
  const [rows, setRows] = useState([])
  const [one, setOne] = useState(null)
  useEffect(() => { api('/api/admin/customers').then(setRows) }, [])
  return (
    <>
      <h1 className="serif">Customers</h1>
      <table className="table">
        <thead><tr><th>Name</th><th>Phone</th><th>Orders</th><th>Status</th></tr></thead>
        <tbody>
          {rows.map((c) => (
            <tr key={c.id} onClick={() => api(`/api/admin/customers/${c.id}`).then(setOne)} style={{ cursor: 'pointer' }}>
              <td>{c.name}</td><td>{c.phone}</td><td>{c.orderCount}</td><td>{c.status}</td>
            </tr>
          ))}
        </tbody>
      </table>
      {one && (
        <div className="form-card" style={{ marginTop: 16 }}>
          <h3>{one.name}</h3>
          <p>{one.phone} · {one.location}</p>
          <p>Total amount ₹{one.totalAmount}</p>
        </div>
      )}
    </>
  )
}

export function ReportsAdmin() {
  const [data, setData] = useState(null)
  useEffect(() => { api('/api/admin/reports').then(setData) }, [])
  if (!data) return <p>Loading…</p>
  return (
    <>
      <h1 className="serif">Reports — {data.month}</h1>
      <div className="kpis">
        <div className="kpi"><b>₹{data.totalSales}</b>Total Sales</div>
        <div className="kpi"><b>{data.goatsSold}</b>Goats Sold</div>
        <div className="kpi"><b>{data.cowsSold}</b>Cows Sold</div>
        <div className="kpi"><b>{data.chickenKg}</b>Chicken KG</div>
        <div className="kpi"><b>{data.completedOrders}</b>Completed Orders</div>
        <div className="kpi"><b>₹{data.monthlySales}</b>This month</div>
      </div>
    </>
  )
}

export function SettingsAdmin() {
  const [form, setForm] = useState({})
  useEffect(() => { api('/api/admin/settings').then(setForm) }, [])
  const set = (k) => (e) => setForm({ ...form, [k]: e.target.value })
  async function save(e) {
    e.preventDefault()
    setForm(await api('/api/admin/settings', { method: 'PUT', body: JSON.stringify(form) }))
    alert('Saved')
  }
  return (
    <form className="form-card" style={{ maxWidth: 560, margin: 0 }} onSubmit={save}>
      <h1 className="serif">Settings</h1>
      <label className="field">WhatsApp number<input value={form.whatsappNumber || ''} onChange={set('whatsappNumber')} /></label>
      <label className="field">Phone<input value={form.phone || ''} onChange={set('phone')} /></label>
      <label className="field">Address<input value={form.address || ''} onChange={set('address')} /></label>
      <label className="field">Hours<input value={form.hours || ''} onChange={set('hours')} /></label>
      <label className="field">Map embed URL<input value={form.mapEmbedUrl || ''} onChange={set('mapEmbedUrl')} /></label>
      <button className="btn btn-primary">Save</button>
    </form>
  )
}
