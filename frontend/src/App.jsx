import { BrowserRouter, Navigate, Route, Routes, useParams } from 'react-router-dom'
import { SpeedInsights } from '@vercel/speed-insights/react'
import { Layout } from './Layout'
import Home from './pages/Home'
import Listing from './pages/Listing'
import Details from './pages/Details'
import Enquiry from './pages/Enquiry'
import Wholesale from './pages/Wholesale'
import HowToOrder, { About, Contact } from './pages/HowToOrder'
import { Faq, Privacy, Refund, Shipping, Terms } from './pages/Policies'
import {
  AdminLogin, AdminShell, CustomersAdmin, Dashboard, EnquiriesAdmin, LivestockAdmin,
  LivestockForm, OrdersAdmin, ReportsAdmin, SettingsAdmin, WholesaleAdmin,
} from './admin'

function EditLivestock() {
  const { id } = useParams()
  return <LivestockForm id={id} />
}

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route element={<Layout />}>
          <Route path="/" element={<Home />} />
          <Route path="/goats" element={<Listing category="GOAT" />} />
          <Route path="/cows" element={<Listing category="COW" />} />
          <Route path="/chicken" element={<Listing category="CHICKEN" />} />
          <Route path="/livestock/:id" element={<Details />} />
          <Route path="/enquiry" element={<Enquiry />} />
          <Route path="/wholesale" element={<Wholesale />} />
          <Route path="/how-to-order" element={<HowToOrder />} />
          <Route path="/about" element={<About />} />
          <Route path="/contact" element={<Contact />} />
          <Route path="/privacy" element={<Privacy />} />
          <Route path="/terms" element={<Terms />} />
          <Route path="/shipping" element={<Shipping />} />
          <Route path="/refund" element={<Refund />} />
          <Route path="/faq" element={<Faq />} />
        </Route>
        <Route path="/admin/login" element={<AdminLogin />} />
        <Route path="/admin" element={<AdminShell />}>
          <Route index element={<Dashboard />} />
          <Route path="livestock" element={<LivestockAdmin />} />
          <Route path="livestock/new" element={<LivestockForm />} />
          <Route path="livestock/:id" element={<EditLivestock />} />
          <Route path="orders" element={<OrdersAdmin />} />
          <Route path="enquiries" element={<EnquiriesAdmin />} />
          <Route path="wholesale" element={<WholesaleAdmin />} />
          <Route path="customers" element={<CustomersAdmin />} />
          <Route path="reports" element={<ReportsAdmin />} />
          <Route path="settings" element={<SettingsAdmin />} />
        </Route>
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
      <SpeedInsights />
    </BrowserRouter>
  )
}
