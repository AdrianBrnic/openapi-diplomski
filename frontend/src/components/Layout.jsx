import { Outlet, NavLink, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { useCart } from '../context/CartContext'



export default function Layout() {
  const { user, logoutUser } = useAuth()
  const navigate = useNavigate()
const { items } = useCart()
  const handleLogout = () => {
    logoutUser()
    navigate('/login')
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <nav className="bg-white border-b border-gray-200 px-6 py-4 flex items-center justify-between">
        <div className="flex items-center gap-8">
          <span className="text-xl font-semibold text-gray-900">WebShop</span>
          <div className="flex gap-1">
            {[
              { to: '/products', label: 'Proizvodi' },
              { to: '/orders', label: 'Narudžbe' },
              { to: '/users', label: 'Korisnici' },
              { to: '/cart', label: `Košarica ${items.length > 0 ? `(${items.reduce((sum, i) => sum + i.quantity, 0)})` : ''}` },].map(({ to, label }) => (
              <NavLink
                key={to}
                to={to}
                className={({ isActive }) =>
                  `px-4 py-2 rounded-lg text-sm font-medium transition-colors ${
                   
                      isActive
                      ? 'bg-gray-100 text-gray-900'
                      : 'text-gray-500 hover:text-gray-900 hover:bg-gray-50'
                  }`
                }
              >
                {label}
              </NavLink>
            ))}
          </div>
        </div>
        <div className="flex items-center gap-4">
          <NavLink to="/profile" className="text-sm text-gray-500 hover:text-gray-900 transition-colors">
           {user?.name}
        </NavLink>
          <button
             onClick={handleLogout}
            className="text-sm text-gray-500 hover:text-gray-900 transition-colors"
          >
            Odjava
          </button>
        </div>
      </nav>
      <main className="max-w-7xl mx-auto px-6 py-8">
        <Outlet />
      </main>
    </div>
  )
}