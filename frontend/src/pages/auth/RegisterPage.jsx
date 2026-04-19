import { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { useAuth } from '../../context/AuthContext'
import { register } from '../../api/auth'

export default function RegisterPage() {
  const [form, setForm] = useState({ name: '', email: '', password: '', phone: '', birthday: '' })
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)
  const { loginUser } = useAuth()
  const navigate = useNavigate()

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value })

  
  const handleSubmit = async (e) => {
    e.preventDefault()
    setLoading(true)
    setError('')
    try {
      const res = await register(form)
      loginUser(res.data.user, res.data.token)
      navigate('/products')
    } catch (err) {
      setError(err.response?.data?.message || 'Greška pri registraciji')
  } finally {
      setLoading(false)
  }
}

  
  
  
  return (
    <div className="min-h-screen bg-gray-50 flex items-center justify-center">
      <div className="bg-white rounded-2xl shadow-sm border border-gray-200 p-8 w-full max-w-md">
        <h1 className="text-2xl font-semibold text-gray-900 mb-2">Registracija</h1>
        <p className="text-gray-500 text-sm mb-8">Kreirajte novi račun</p>
        <form onSubmit={handleSubmit} className="flex flex-col gap-4">
          {[
            { name: 'name', label: 'Ime i prezime', type: 'text', placeholder: 'Ime i prezime' },
            { name: 'email', label: 'Email', type: 'email', placeholder: 'E-mail' },
            { name: 'password', label: 'Lozinka', type: 'password', placeholder: 'Lozinka' },
            { name: 'phone', label: 'Telefon', type: 'text', placeholder: 'Telefon' },
            
            { name: 'birthday', label: 'Datum rođenja', type: 'date', placeholder: 'Datum rođenja' },
          ].map(({ name, label, type, placeholder }) => (
            <div key={name}>
              <label className="block text-sm font-medium text-gray-700 mb-1">{label}</label>
              <input
                type={type}
                name={name}
                value={form[name]}
                onChange={handleChange}
                className="w-full border border-gray-200 rounded-lg px-4 py-2.5 text-sm focus:outline-none focus:ring-2 focus:ring-gray-900 focus:border-transparent"
                placeholder={placeholder}
                required={['name', 'email', 'password'].includes(name)}
              />
            </div>
          ))}
          {error && <p className="text-red-500 text-sm">{error}</p>}
          
          <button
            type="submit"
            disabled={loading}
            className="bg-gray-900 text-white rounded-lg py-2.5 text-sm font-medium hover:bg-gray-700 transition-colors disabled:opacity-50"
          >
            {loading ? 'Kreiranje računa...' : 'Registracija'}
          </button>
        </form>
        <p className="text-sm text-gray-500 mt-6 text-center">
          Već imate račun?{' '}
          <Link to="/login" className="text-gray-900 font-medium hover:underline">
            Prijava
          </Link>
        </p>
      </div>
  </div>
 )
}