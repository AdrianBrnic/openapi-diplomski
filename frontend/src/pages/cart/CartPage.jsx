import { useCart } from '../../context/CartContext'
import { useAuth } from '../../context/AuthContext'
import { useMutation } from '@tanstack/react-query'
import { createOrder } from '../../api/orders'
import { useNavigate } from 'react-router-dom'


export default function CartPage() {
  const { items, removeItem, updateQuantity, clearCart, total } = useCart()
  const { user } = useAuth()
  const navigate = useNavigate()

   const createMutation = useMutation({
    mutationFn: createOrder,
    onSuccess: () => {
      clearCart()
      navigate('/orders')
    },
  })

  
  
    const handleCheckout = () => {
    createMutation.mutate({
      userId: user.id,
      items: items.map(i => ({ productId: i.id, quantity: i.quantity })),
    })
  }

  
  if (items.length === 0) {
    return (
      <div>
         <h1 className="text-2xl font-semibold text-gray-900 mb-6">Košarica</h1>
          <div className="bg-white border border-gray-200 rounded-2xl p-12 text-center">
        <p className="text-gray-400 text-sm">Košarica je prazna</p>
        </div>
      </div>
    )
  }

  return (
    <div>
      <h1 className="text-2xl font-semibold text-gray-900 mb-6">Košarica</h1>
      <div className="grid grid-cols-3 gap-6">
        <div className="col-span-2">
          <div className="bg-white border border-gray-200 rounded-2xl overflow-hidden">
            <table className="w-full">
              <thead>
                <tr className="border-b border-gray-100">
                  {['Proizvod', 'Cijena', 'Količina', 'Ukupno', ''].map(h => (
                    <th key={h} className="text-left px-6 py-4 text-xs font-medium text-gray-500 uppercase tracking-wider">{h}</th>
                  ))}
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-50">
                {items.map(item => (
                  <tr key={item.id}>
                    <td className="px-6 py-4 text-sm font-medium text-gray-900">{item.name}</td>
                    <td className="px-6 py-4 text-sm text-gray-500">€{item.price.toFixed(2)}</td>
                    <td className="px-6 py-4">
                      <div className="flex items-center gap-2">
                        <button
                          onClick={() => updateQuantity(item.id, item.quantity - 1)}
                          className="w-7 h-7 flex items-center justify-center border border-gray-200 rounded-lg text-gray-500 hover:bg-gray-50 transition-colors"
                        >
                          -
                        </button>
                        <span className="text-sm w-6 text-center">{item.quantity}</span>
                        <button
                          onClick={() => updateQuantity(item.id, item.quantity + 1)}
                          className="w-7 h-7 flex items-center justify-center border border-gray-200 rounded-lg text-gray-500 hover:bg-gray-50 transition-colors"
                        >
                          +
                        </button>
                      </div>
                    </td>
                    <td className="px-6 py-4 text-sm text-gray-900">€{(item.price * item.quantity).toFixed(2)}</td>
                    <td className="px-6 py-4 text-right">
                      <button
                        onClick={() => removeItem(item.id)}
                        className="text-sm text-red-400 hover:text-red-600 transition-colors"
                      >
                        Ukloni
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>

        <div className="col-span-1">
          <div className="bg-white border border-gray-200 rounded-2xl p-6">
            <h2 className="text-lg font-medium text-gray-900 mb-4">Pregled narudžbe</h2>
            <div className="flex flex-col gap-3 mb-6">
              {items.map(item => (
                <div key={item.id} className="flex justify-between text-sm">
                   <span className="text-gray-500">{item.name} × {item.quantity}</span>
                   <span className="text-gray-900">€{(item.price * item.quantity).toFixed(2)}</span>
                 </div>
              
              ))}
              <div className="border-t border-gray-100 pt-3 flex justify-between">
                <span className="font-medium text-gray-900">Ukupno</span>
                <span className="font-medium text-gray-900">€{total.toFixed(2)}</span>
              </div>
            </div>
            
            
            {createMutation.isError && (
              <p className="text-red-500 text-sm mb-3">
                {createMutation.error?.response?.data?.message || 'Greška pri narudžbi'}
              </p>
            )}
            
            
            <button
              onClick={handleCheckout}
              disabled={createMutation.isPending}
              className="w-full bg-gray-900 text-white py-2.5 rounded-lg text-sm font-medium hover:bg-gray-700 transition-colors disabled:opacity-50"
            >
              {createMutation.isPending ? 'Procesiranje...' : 'Dovršite narudžbu'}
            </button>
            
            
            <button
              onClick={clearCart}
              className="w-full mt-2 border border-gray-200 text-gray-600 py-2.5 rounded-lg text-sm font-medium hover:bg-gray-50 transition-colors"
            >
              Isprazni košaricu
            </button>
          </div>
        </div>
      </div>
    </div>
  )
}