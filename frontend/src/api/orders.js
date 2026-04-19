import client from './client'

export const getOrders = (params) => client.get('/orders', { params })
export const getOrderById = (id) => client.get(`/orders/${id}`)
export const createOrder = (data) => client.post('/orders', data)
export const updateOrderStatus = (id, data) => client.patch(`/orders/${id}`, data)
export const deleteOrder = (id) => client.delete(`/orders/${id}`)