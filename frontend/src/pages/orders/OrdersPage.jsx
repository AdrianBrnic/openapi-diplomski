import { useState } from 'react'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { getOrders, createOrder, updateOrderStatus, deleteOrder } from '../../api/orders'
import { getProducts } from '../../api/products'
import { getUsers } from '../../api/users'

const STATUS_LABELS = {
  CREATED: 'Kreirana',
  PAID: 'Plaćena',
  SHIPPED: 'Poslana',
  DELIVERED: 'Dostavljena',
  CANCELLED: 'Otkazana',
}

 const STATUS_COLORS = {
  CREATED: 'bg-blue-50 text-blue-700',
  PAID: 'bg-green-50 text-green-700',
  SHIPPED: 'bg-yellow-50 text-yellow-700',
  DELIVERED: 'bg-gray-100 text-gray-700',
  CANCELLED: 'bg-red-50 text-red-700',
}

const NEXT_STATUS = {
  CREATED: 'PAID',
  PAID: 'SHIPPED',
  SHIPPED: 'DELIVERED',
}



export default function OrdersPage() {
  const queryClient = useQueryClient()
  const [page, setPage] = useState(0)
  const [showForm, setShowForm] = useState(false)
  const [userId, setUserId] = useState('')
  const [items, setItems] = useState([{ productId: '', quantity: 1 }])
  const [expanded, setExpanded] = useState(null)

  const { data, isLoading } = useQuery({
    queryKey: ['orders', page],
    queryFn: () => getOrders({ page, size: 10 }).then(r => r.data),
  })


  const { data: productsData } = useQuery({
    queryKey: ['products-all'],
    queryFn: () => getProducts({ size: 100 }).then(r => r.data),
  })

  const createMutation = useMutation({
    mutationFn: createOrder,
    onSuccess: () => {
      queryClient.invalidateQueries(['orders'])
      queryClient.invalidateQueries(['products'])
      setShowForm(false)
      setUserId('')
      setItems([{ productId: '', quantity: 1 }])
    },
  })

  const updateStatusMutation = useMutation({
    mutationFn: ({ id, status }) => updateOrderStatus(id, { status }),
    onSuccess: () => queryClient.invalidateQueries(['orders']),
  })



  const { data: usersData } = useQuery({
    queryKey: ['users-all'],
    queryFn: () => getUsers({ size: 100 }).then(r => r.data),
  })

  
  
  const deleteMutation = useMutation({
    mutationFn: deleteOrder,
    onSuccess: () => queryClient.invalidateQueries(['orders']),
  })

   const handleAddItem = () => setItems([...items, { productId: '', quantity: 1 }])

  const handleItemChange = (index, field, value) => {
    const updated = [...items]
    updated[index][field] = value
    setItems(updated)
  }

  const handleRemoveItem = (index) => setItems(items.filter((_, i) => i !== index))

  const handleSubmit = (e) => {
    e.preventDefault()
    createMutation.mutate({
      userId: parseInt(userId),
      items: items.map(i => ({ productId: parseInt(i.productId), quantity: parseInt(i.quantity) })),
    })
  }

   return (
    <div>
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-2xl font-semibold text-gray-900">Narudžbe</h1>
        <button
          onClick={() => setShowForm(!showForm)}
          className="bg-gray-900 text-white px-4 py-2 rounded-lg text-sm font-medium hover:bg-gray-700 transition-colors"
        >
          + Nova narudžba
        </button>
      </div>

      
      
       {showForm && (
        <div className="bg-white border border-gray-200 rounded-2xl p-6 mb-6">
          <h2 className="text-lg font-medium text-gray-900 mb-4">Nova narudžba</h2>
          <form onSubmit={handleSubmit} className="flex flex-col gap-4">
            <div className="w-96">
              <label className="block text-sm font-medium text-gray-700 mb-1">Korisnik</label>
              <select
              value={userId}
              onChange={(e) => setUserId(e.target.value)}
              className="w-full border border-gray-200 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-gray-900"
            required
            >
    <option value="">Odaberi korisnika</option>
    {usersData?.content?.map(u => (
      <option key={u.id} value={u.id}>
        {u.name} — {u.email}
      </option>
    ))}
  </select>
</div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">Stavke</label>
              <div className="flex flex-col gap-2">
                {items.map((item, index) => (
                  <div key={index} className="flex gap-3 items-center">
                    <select
                      value={item.productId}
                      onChange={(e) => handleItemChange(index, 'productId', e.target.value)}
                      className="flex-1 border border-gray-200 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-gray-900"
                      required
                    >
                      <option value="">Odaberi proizvod</option>
                      {productsData?.content?.map(p => (
                        <option key={p.id} value={p.id}>
                          {p.name} — €{p.price.toFixed(2)} (zalihe: {p.stock})
                        </option>
                      ))}
                    </select>
                    <input
                      type="number"
                      min="1"
                      value={item.quantity}
                      onChange={(e) => handleItemChange(index, 'quantity', e.target.value)}
                      className="w-20 border border-gray-200 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-gray-900"
                      required
                    />
                    
                    
                    
                    {items.length > 1 && (
                      <button
                        type="button"
                        onClick={() => handleRemoveItem(index)}
                        className="text-red-400 hover:text-red-600 text-sm transition-colors"
                        >
                        Ukloni
                      </button>
                    )}
                  </div>
                ))}
              </div>
              <button
                type="button"
                onClick={handleAddItem}
                className="mt-2 text-sm text-gray-500 hover:text-gray-900 transition-colors"
              >
                + Dodaj stavku
              </button>
            </div>

            
            
            {createMutation.isError && (
              <p className="text-red-500 text-sm">
                {createMutation.error?.response?.data?.message || 'Greška pri kreiranju narudžbe'}
              </p>
            )}

            
            <div className="flex gap-3">
              <button
                type="submit"
                className="bg-gray-900 text-white px-4 py-2 rounded-lg text-sm font-medium hover:bg-gray-700 transition-colors"
              >
                Kreiraj narudžbu
              </button>
              <button
                type="button"
                onClick={() => setShowForm(false)}
                className="border border-gray-200 px-4 py-2 rounded-lg text-sm font-medium text-gray-600 hover:bg-gray-50 transition-colors"
              >
                Odustani
              </button>
            </div>
          </form>
        </div>
      )}

      
      
      {isLoading ? (
        <div className="text-center py-12 text-gray-400">Učitavanje...</div>
      ) : (
        <>
          <div className="bg-white border border-gray-200 rounded-2xl overflow-hidden">
            <table className="w-full">
              <thead>
                <tr className="border-b border-gray-100">
                  {['ID', 'Korisnik', 'Ukupno', 'Status', 'Datum', ''].map(h => (
                    <th key={h} className="text-left px-6 py-4 text-xs font-medium text-gray-500 uppercase tracking-wider">{h}</th>
                  ))}
                </tr>
              </thead>
               <tbody className="divide-y divide-gray-50">
                
                {data?.content?.map(order => (
                  <>
                    <tr
                      key={order.id}
                      className="hover:bg-gray-50 transition-colors cursor-pointer"
                      onClick={() => setExpanded(expanded === order.id ? null : order.id)}
                    >
                      <td className="px-6 py-4 text-sm text-gray-500">#{order.id}</td>
                      <td className="px-6 py-4 text-sm font-medium text-gray-900">
                       {usersData?.content?.find(u => u.id === order.userId)?.name || `Korisnik #${order.userId}`}
                      </td>
                      <td className="px-6 py-4 text-sm text-gray-900">€{order.totalPrice.toFixed(2)}</td>
                      <td className="px-6 py-4">
                        <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${STATUS_COLORS[order.status]}`}>
                          {STATUS_LABELS[order.status]}
                        </span>
                      </td>
                      <td className="px-6 py-4 text-sm text-gray-500">
                        {new Date(order.createdAt).toLocaleDateString('hr-HR')}
                      </td>
                      <td className="px-6 py-4 text-right">
                        <div className="flex justify-end gap-2" onClick={e => e.stopPropagation()}>
                          {NEXT_STATUS[order.status] && (
                            <button
                              onClick={() => updateStatusMutation.mutate({ id: order.id, status: NEXT_STATUS[order.status] })}
                              className="text-sm text-gray-500 hover:text-gray-900 transition-colors"
                            >
                              → {STATUS_LABELS[NEXT_STATUS[order.status]]}
                            </button>
                          )}
                          <button
                            onClick={() => deleteMutation.mutate(order.id)}
                            className="text-sm text-red-400 hover:text-red-600 transition-colors"
                          >
                            Obriši
                          </button>
                        </div>
                      </td>
                    </tr>
                    {expanded === order.id && (
                      <tr key={`${order.id}-expanded`} className="bg-gray-50">
                        <td colSpan={6} className="px-6 py-4">
                          <table className="w-full">
                            <thead>
                              <tr>
                                {['Proizvod', 'Cijena', 'Količina', 'Ukupno'].map(h => (
                                  <th key={h} className="text-left text-xs font-medium text-gray-400 uppercase pb-2">{h}</th>
                                ))}
                              </tr>
                            </thead>
                            <tbody>
                              {order.items.map(item => (
                                <tr key={item.id}>
                                  <td className="text-sm text-gray-700 py-1">{item.productName}</td>
                                  <td className="text-sm text-gray-500 py-1">€{item.unitPrice.toFixed(2)}</td>
                                  <td className="text-sm text-gray-500 py-1">{item.quantity}</td>
                                  <td className="text-sm text-gray-700 py-1">€{item.subtotal.toFixed(2)}</td>
                                </tr>
                              ))}
                            </tbody>
                          </table>
                        </td>
                      </tr>
                    )}
                  </>
                ))}
              </tbody>
            </table>
          </div>

          {data?.page && (
            <div className="flex items-center justify-between mt-4">
              <p className="text-sm text-gray-500">
                Ukupno {data.page.totalElements} narudžbi
              </p>
              <div className="flex gap-2">
                <button
                  onClick={() => setPage(p => p - 1)}
                  disabled={page === 0}
                  className="px-3 py-1.5 text-sm border border-gray-200 rounded-lg disabled:opacity-40 hover:bg-gray-50 transition-colors"
                >
                  Prethodna
                
                </button>
                <span className="px-3 py-1.5 text-sm text-gray-500">
                  {page + 1} / {data.page.totalPages}
                </span>
                
                <button
                  onClick={() => setPage(p => p + 1)}
                  disabled={page + 1 >= data.page.totalPages}
                  className="px-3 py-1.5 text-sm border border-gray-200 rounded-lg disabled:opacity-40 hover:bg-gray-50 transition-colors"
                >
                  Sljedeća
                </button>
              </div>
            </div>
          )}
        </>
      )}
    </div>
  )
}