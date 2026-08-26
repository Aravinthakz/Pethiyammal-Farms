import { PolicyPage } from '../Layout'

export function Privacy() {
  return <PolicyPage title="Privacy Policy"><p>We collect name, phone and enquiry details only to respond to livestock orders. We do not sell customer data.</p></PolicyPage>
}
export function Terms() {
  return <PolicyPage title="Terms & Conditions"><p>Listings show current farm availability. Prices can change. Advance payment confirms a reserved animal. Individual animals cannot be sold twice once marked sold.</p></PolicyPage>
}
export function Shipping() {
  return <PolicyPage title="Shipping Policy"><p>Pickup is available at Namakkal. Delivery can be arranged after confirmation. Transport charges depend on location and animal type.</p></PolicyPage>
}
export function Refund() {
  return <PolicyPage title="Refund Policy"><p>Advances are discussed case by case if an animal becomes unavailable before confirmation. Sold animals are not returnable after delivery unless otherwise agreed.</p></PolicyPage>
}
export function Faq() {
  return (
    <PolicyPage title="FAQ">
      <p><strong>Do I need an account?</strong> No. Browse and enquire on WhatsApp or the form.</p>
      <p><strong>How is chicken priced?</strong> Country chicken is sold per KG with a minimum order.</p>
      <p><strong>Can I buy wholesale?</strong> Yes. Use the wholesale form for hotels, shops and traders.</p>
    </PolicyPage>
  )
}
