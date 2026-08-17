import { useState } from 'react'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { getProducts, createProduct, updateProduct, deleteProduct } from '../../api/products'
import { useCart } from '../../context/CartContext'



export default function ProductsPage() {
  const queryClient = useQueryClient()
  const [page, setPage] = useState(0)
  const [showForm, setShowForm] = useState(false)
  const [editing, setEditing] = useState(null)
  const [form, setForm] = useState({ name: '', description: '', price: '', stock: '' })

  const { data, isLoading } = useQuery({
    queryKey: ['products', page],
    queryFn: () => getProducts({ page, size: 10 }).then(r => r.data),
  })

  const createMutation = useMutation({
    mutationFn: createProduct,
    onSuccess: () => { queryClient.invalidateQueries(['products']); resetForm() },
  })

  const updateMutation = useMutation({
    mutationFn: ({ id, data }) => updateProduct(id, data),
    onSuccess: () => { queryClient.invalidateQueries(['products']); resetForm() },
  })

  const deleteMutation = useMutation({
    mutationFn: deleteProduct,
    onSuccess: () => queryClient.invalidateQueries(['products']),
  })

  const resetForm = () => {
    setForm({ name: '', description: '', price: '', stock: '' })
    setEditing(null)
    setShowForm(false)
  }

  const handleEdit = (product) => {
    setEditing(product)
    setForm({ name: product.name, description: product.description || '', price: product.price, stock: product.stock })
    setShowForm(true)
  }

  const { addItem } = useCart()

  const handleSubmit = (e) => {
    e.preventDefault()
    const data = { ...form, price: parseFloat(form.price), stock: parseInt(form.stock) }
    if (editing) {
      updateMutation.mutate({ id: editing.id, data })
    } else {
      createMutation.mutate(data)
    }
}

  
  
  return (
    <div>
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-2xl font-semibold text-gray-900">Proizvodi</h1>
        <button
          onClick={() => { resetForm(); setShowForm(true) }}
          className="bg-gray-900 text-white px-4 py-2 rounded-lg text-sm font-medium hover:bg-gray-700 transition-colors"
        >
          + Novi proizvod
        </button>
      </div>

      {showForm && (
        <div className="bg-white border border-gray-200 rounded-2xl p-6 mb-6">
          <h2 className="text-lg font-medium text-gray-900 mb-4">
              {editing ? 'Uredi proizvod' : 'Novi proizvod'}
          </h2>
          <form onSubmit={handleSubmit} className="grid grid-cols-2 gap-4">
            {[
              { name: 'name', label: 'Naziv', type: 'text' },
              { name: 'price', label: 'Cijena (€)', type: 'number' },
              { name: 'stock', label: 'Zalihe', type: 'number' },
            ].map(({ name, label, type }) => (
              <div key={name}>
                <label className="block text-sm font-medium text-gray-700 mb-1">{label}</label>
                <input
                  type={type}
                  value={form[name]}
                  onChange={(e) => setForm({ ...form, [name]: e.target.value })}
                  className="w-full border border-gray-200 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-gray-900"
                  required
                />
              </div>
            ))}
            <div className="col-span-2">
              <label className="block text-sm font-medium text-gray-700 mb-1">Opis</label>
              <textarea
                value={form.description}
                onChange={(e) => setForm({ ...form, description: e.target.value })}
                className="w-full border border-gray-200 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-gray-900"
                rows={2}
              />
            </div>
            <div className="col-span-2 flex gap-3">
              <button
                type="submit"
                className="bg-gray-900 text-white px-4 py-2 rounded-lg text-sm font-medium hover:bg-gray-700 transition-colors"
              >
                {editing ? 'Spremi' : 'Dodaj'}
              </button>
              <button
                type="button"
                onClick={resetForm}
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
                  {['Naziv', 'Opis', 'Cijena', 'Zalihe', ''].map(h => (
                    <th key={h} className="text-left px-6 py-4 text-xs font-medium text-gray-500 uppercase tracking-wider">{h}</th>
                  ))}
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-50">
                {data?.content?.map(product => (
                  <tr key={product.id} className="hover:bg-gray-50 transition-colors">
                    <td className="px-6 py-4 text-sm font-medium text-gray-900">{product.name}</td>
                    <td className="px-6 py-4 text-sm text-gray-500 max-w-xs truncate">{product.description || '—'}</td>
                    <td className="px-6 py-4 text-sm text-gray-900">€{product.price.toFixed(2)}</td>
                    <td className="px-6 py-4">
                      <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${
                        product.stock > 10 ? 'bg-green-50 text-green-700' :
                        product.stock > 0 ? 'bg-yellow-50 text-yellow-700' :
                        'bg-red-50 text-red-700'
                      }`}>
                        {product.stock}
                      </span>
                    </td>
                    <td className="px-6 py-4 text-right">
                      <div className="flex justify-end gap-2">
                        <button
                           onClick={(e) => { e.stopPropagation(); addItem(product) }}
                           className="text-sm text-blue-500 hover:text-blue-700 transition-colors"
                          >
                         Dodaj u košaricu
                       </button>
                        <button
                          onClick={() => handleEdit(product)}
                          className="text-sm text-gray-500 hover:text-gray-900 transition-colors"
                        >
                          Uredi
                        </button>
                        <button
                          onClick={() => deleteMutation.mutate(product.id)}
                          className="text-sm text-red-400 hover:text-red-600 transition-colors"
                        >
                          Obriši
                      </button>
                        
                      </div>
                    </td>
                   </tr>
                ))}
              </tbody>
            </table>
      </div>

          
          
          {data?.page && (
            <div className="flex items-center justify-between mt-4">
              <p className="text-sm text-gray-500">
                Ukupno {data.page.totalElements} proizvoda
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