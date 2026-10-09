import axios from 'axios';

const API_BASE_URL = 'http://127.0.0.1:8080/api';

const api = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});

export const catalogApi = {
    getCategories: () => api.get('/catalog/categories'),
    createCategory: (data) => api.post('/catalog/categories', data),
    updateCategory: (id, data) => api.put(`/catalog/categories/${id}`, data),
    deleteCategory: (id) => api.delete(`/catalog/categories/${id}`),

    getProducts: () => api.get('/catalog/products'),
    getProduct: (id) => api.get(`/catalog/products/${id}`),
    createProduct: (data) => api.post('/catalog/products', data),
    updateProduct: (id, data) => api.put(`/catalog/products/${id}`, data),
    deleteProduct: (id) => api.delete(`/catalog/products/${id}`),
};

export const billingApi = {
    createBill: (data) => api.post('/billing/bills', data),
    getBill: (id) => api.get(`/billing/bills/${id}`),
    getBillItems: (billId) => api.get(`/billing/bill-items/${billId}`),
};

export const storeApi = {
    getInventory: () => api.get('/store/inventory'),
    getInventoryItem: (productId) => api.get(`/store/inventory/${productId}`),
    updateStock: (productId, quantity) => api.put(`/store/inventory/${productId}/stock`, quantity),
    getLowStockItems: () => api.get('/store/inventory/low-stock'),
};

export const customerApi = {
    getCustomers: () => api.get('/customers'),
    getCustomer: (id) => api.get(`/customers/${id}`),
    createCustomer: (data) => api.post('/customers', data),
    updateCustomer: (id, data) => api.put(`/customers/${id}`, data),
    deleteCustomer: (id) => api.delete(`/customers/${id}`),
    getBalance: (id) => api.get(`/customers/${id}/balance`),
};

export const financialApi = {
    makePayment: (data) => api.post('/customers/payments', data),
    getCustomerLedger: (customerId) => api.get(`/customers/${customerId}/statement`),
};

export default api;
