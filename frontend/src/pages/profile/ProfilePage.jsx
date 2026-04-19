import { useAuth } from '../../context/AuthContext'
import { useState } from 'react'
import { useMutation, useQueryClient } from '@tanstack/react-query'
import { updateUser } from '../../api/users'

export default function ProfilePage() {
  const { user, loginUser } = useAuth()
  const queryClient = useQueryClient()
  const [editing, setEditing] = useState(false)
  const [form, setForm] = useState({
    name: user?.name || '',
    phone: user?.phone || '',
    birthday: user?.birthday || '',
  })

  const updateMutation = useMutation({
    mutationFn: (data) => updateUser(user.id, data),
    onSuccess: (res) => {
      loginUser(res.data, localStorage.getItem('token'))
      queryClient.invalidateQueries(['users'])
      setEditing(false)
    },
  })

  const handleSubmit = (e) => {
    e.preventDefault()
    updateMutation.mutate(form)
  }

  return (
    <div className="max-w-2xl">
      <h1 className="text-2xl font-semibold text-gray-900 mb-6">Profil</h1>
      <div className="bg-white border border-gray-200 rounded-2xl p-6">
        {!editing ? (
          <div className="flex flex-col gap-4">
            {[
              { label: 'Ime i prezime', value: user?.name },
              { label: 'Email', value: user?.email },
              { label: 'Telefon', value: user?.phone || '—' },
              { label: 'Datum rođenja', value: user?.birthday || '—' },
              { label: 'Registriran', value: user?.createdAt ? new Date(user.createdAt).toLocaleDateString('hr-HR') : '—' },
            ].map(({ label, value }) => (
              <div key={label} className="flex justify-between py-3 border-b border-gray-50 last:border-0">
                <span className="text-sm text-gray-500">{label}</span>
                <span className="text-sm font-medium text-gray-900">{value}</span>
              </div>
            ))}
            <button
              onClick={() => setEditing(true)}
              className="mt-2 bg-gray-900 text-white px-4 py-2 rounded-lg text-sm font-medium hover:bg-gray-700 transition-colors w-fit"
            >
              Uredi profil
            </button>
          </div>
        ) : (
          <form onSubmit={handleSubmit} className="flex flex-col gap-4">
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
            {updateMutation.isError && (
              <p className="text-red-500 text-sm">Greška pri ažuriranju profila</p>
            )}
            <div className="flex gap-3">
              <button
                type="submit"
                disabled={updateMutation.isPending}
                className="bg-gray-900 text-white px-4 py-2 rounded-lg text-sm font-medium hover:bg-gray-700 transition-colors disabled:opacity-50"
              >
                Spremi
              </button>
              <button
                type="button"
                onClick={() => setEditing(false)}
                className="border border-gray-200 px-4 py-2 rounded-lg text-sm font-medium text-gray-600 hover:bg-gray-50 transition-colors"
              >
                Odustani
              </button>
            </div>
          </form>
        )}
      </div>
    </div>
  )
}