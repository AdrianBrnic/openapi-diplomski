import { useState } from 'react'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { getUsers, updateUser, deleteUser } from '../../api/users'

export default function UsersPage() {
  const queryClient = useQueryClient()
  const [page, setPage] = useState(0)
  const [editing, setEditing] = useState(null)
  const [form, setForm] = useState({ name: '', phone: '', birthday: '' })

  const { data, isLoading } = useQuery({
    queryKey: ['users', page],
    queryFn: () => getUsers({ page, size: 10 }).then(r => r.data),
  })

  const updateMutation = useMutation({
    mutationFn: ({ id, data }) => updateUser(id, data),
    onSuccess: () => { queryClient.invalidateQueries(['users']); resetForm() },
  })

  const deleteMutation = useMutation({
    mutationFn: deleteUser,
    onSuccess: () => queryClient.invalidateQueries(['users']),
  })

  const resetForm = () => {
    setEditing(null)
    setForm({ name: '', phone: '', birthday: '' })
  }

  const handleEdit = (user) => {
  setEditing(user)
  setForm({ name: user.name, phone: user.phone || '', birthday: user.birthday || '' })
  }

  const handleSubmit = (e) => {
    e.preventDefault()
    updateMutation.mutate({ id: editing.id, data: form })
  }

  return (
    <div>
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-2xl font-semibold text-gray-900">Korisnici</h1>
      </div>

      {editing && (
        <div className="bg-white border border-gray-200 rounded-2xl p-6 mb-6">
          <h2 className="text-lg font-medium text-gray-900 mb-4">Uredi korisnika</h2>
          <form onSubmit={handleSubmit} className="grid grid-cols-3 gap-4">
            {[
              { name: 'name', label: 'Ime i prezime', type: 'text' },
              { name: 'phone', label: 'Telefon', type: 'text' },
              { name: 'birthday', label: 'Datum rođenja', type: 'date' },
            ].map(({ name, label, type }) => (
              <div key={name}>
                <label className="block text-sm font-medium text-gray-700 mb-1">{label}</label>
                <input
                  type={type}
                  value={form[name]}
                  onChange={(e) => setForm({ ...form, [name]: e.target.value })}
                  className="w-full border border-gray-200 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-gray-900"
                />
              </div>
            ))}
            <div className="col-span-3 flex gap-3">
              <button
                type="submit"
                className="bg-gray-900 text-white px-4 py-2 rounded-lg text-sm font-medium hover:bg-gray-700 transition-colors"
              >
                Spremi
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
                  {['Ime', 'Email', 'Telefon', 'Datum rođenja', 'Registriran', ''].map(h => (
                    <th key={h} className="text-left px-6 py-4 text-xs font-medium text-gray-500 uppercase tracking-wider">{h}</th>
                  ))}
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-50">
                {data?.content?.map(user => (
                  <tr key={user.id} className="hover:bg-gray-50 transition-colors">
                    <td className="px-6 py-4 text-sm font-medium text-gray-900">{user.name}</td>
                    <td className="px-6 py-4 text-sm text-gray-500">{user.email}</td>
                    <td className="px-6 py-4 text-sm text-gray-500">{user.phone || '—'}</td>

                    <td className="px-6 py-4 text-sm text-gray-500">{user.birthday || '—'}</td>
                    <td className="px-6 py-4 text-sm text-gray-500">
                      {new Date(user.createdAt).toLocaleDateString('hr-HR')}
                    </td>
                    <td className="px-6 py-4 text-right">
                      <div className="flex justify-end gap-2">
                        <button
                          onClick={() => handleEdit(user)}
                          className="text-sm text-gray-500 hover:text-gray-900 transition-colors"
                        >
                          Uredi
                        </button>
                        <button
                          onClick={() => deleteMutation.mutate(user.id)}
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
                Ukupno {data.page.totalElements} korisnika
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