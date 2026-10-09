import React, { useState, useEffect } from 'react';
import { Search, Trash2, Plus, Minus, CheckCircle, UserPlus, CreditCard, Banknote, Loader2 } from 'lucide-react';
import { useCartStore } from '../store/cartStore';
import { catalogApi, customerApi, billingApi } from '../api/api';

const Billing = () => {
  const {
    cart, addToCart, updateQuantity, removeFromCart,
    customerId, setCustomerId, discount, setDiscount, clearCart
  } = useCartStore();

  const [customers, setCustomers] = useState([]);
  const [products, setProducts] = useState([]);
  const [searchQuery, setSearchQuery] = useState('');
  const [filteredProducts, setFilteredProducts] = useState([]);
  const [isSearching, setIsSearching] = useState(false);
  const [amountPaid, setAmountPaid] = useState('');
  const [paymentMode, setPaymentMode] = useState('PAID');
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [successMessage, setSuccessMessage] = useState('');

  const [isCustomerModalOpen, setIsCustomerModalOpen] = useState(false);
  const [newCustomer, setNewCustomer] = useState({ name: '', phone: '', email: '', address: '' });

  useEffect(() => {
    loadInitialData();
  }, []);

  const loadInitialData = async () => {
    try {
      const [custRes, prodRes] = await Promise.all([
        customerApi.getCustomers(),
        catalogApi.getProducts()
      ]);
      setCustomers(custRes.data || []);
      setProducts(prodRes.data || []);
    } catch (error) {
      console.error("Failed to load initial data", error);
    }
  };

  const handleSearch = async (e) => {
    const query = e.target.value;
    setSearchQuery(query);

    if (!query) {
      setFilteredProducts([]);
      return;
    }

    setIsSearching(true);
    try {
      const results = products.filter(p =>
        p.name?.toLowerCase().includes(query.toLowerCase()) ||
        p.sku?.toLowerCase().includes(query.toLowerCase()) ||
        p.id?.toString() === query
      ).slice(0, 5);
      setFilteredProducts(results);
    } catch (err) {
      console.error("Search error", err);
    } finally {
      setIsSearching(false);
    }
  };

  const selectProduct = (product) => {
    addToCart(product);
    setSearchQuery('');
    setFilteredProducts([]);
  };

  const calculateSubtotal = () => {
    return cart.reduce((sum, item) => sum + (item.price * item.quantity), 0);
  };

  const calculateTotal = () => {
    const subtotal = calculateSubtotal();
    return Math.max(0, subtotal - discount);
  };

  const handleAddQuickCustomer = async (e) => {
    e.preventDefault();
    if (!newCustomer.name || !newCustomer.phone) {
      alert("Name and Phone are required!");
      return;
    }
    try {
      const res = await customerApi.createCustomer(newCustomer);
      const createdCustomer = res.data;
      setCustomers(prev => [...prev, createdCustomer]);
      setCustomerId(createdCustomer.id);
      setIsCustomerModalOpen(false);
      setNewCustomer({ name: '', phone: '', email: '', address: '' });
    } catch (error) {
      alert("Error creating customer: " + (error.response?.data?.message || error.message));
    }
  };

  const handleCompleteSale = async () => {
    if (!customerId) {
      alert("Please select or create a customer");
      return;
    }
    if (cart.length === 0) {
      alert("Cart is empty");
      return;
    }

    setIsSubmitting(true);
    try {
      const billRequest = {
        customerId: parseInt(customerId),
        amountPaid: paymentMode === 'PAID' ? (parseFloat(amountPaid) || 0) : 0,
        items: cart.map(item => ({
          productId: item.id,
          quantity: item.quantity
        }))
      };

      await billingApi.createBill(billRequest);
      setSuccessMessage("Sale completed successfully!");
      clearCart();
      setAmountPaid('');
      setPaymentMode('PAID');
      setTimeout(() => setSuccessMessage(''), 3000);
    } catch (error) {
      alert("Error creating bill: " + (error.response?.data?.message || error.message));
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-3xl font-bold text-gray-800">Billing</h1>
        <div className="text-sm text-gray-500">POS Terminal #1</div>
      </div>

      {successMessage && (
        <div className="p-4 bg-green-100 text-green-700 rounded-lg flex items-center gap-2 animate-bounce">
          <CheckCircle size={20} />
          {successMessage}
        </div>
      )}

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Cart / Items List */}
        <div className="lg:col-span-2 bg-white rounded-2xl shadow-sm border border-gray-200 overflow-hidden flex flex-col h-[70vh]">
          <div className="p-4 bg-gray-50 border-b border-gray-200 font-semibold text-gray-700">
            Current Order
          </div>
          <div className="flex-1 overflow-auto p-4">
            <table className="w-full text-left">
              <thead className="text-gray-400 text-sm uppercase tracking-wider border-b border-gray-100">
                <tr>
                  <th className="pb-3 font-medium">Product</th>
                  <th className="pb-3 font-medium text-center">Qty</th>
                  <th className="pb-3 font-medium text-right">Price</th>
                  <th className="pb-3 font-medium text-right">Subtotal</th>
                  <th className="pb-3 font-medium text-center">Action</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-50">
                {cart.length === 0 ? (
                  <tr>
                    <td colSpan="5" className="py-10 text-center text-gray-400 italic">
                      No items added yet. Search for a product to start.
                    </td>
                  </tr>
                ) : (
                  cart.map(item => (
                    <tr key={item.id} className="group hover:bg-gray-50 transition-colors">
                      <td className="py-4 font-medium text-gray-700">{item.name}</td>
                      <td className="py-4">
                        <div className="flex items-center justify-center gap-2">
                          <button onClick={() => updateQuantity(item.id, -1)} className="p-1 hover:bg-gray-200 rounded"><Minus size={14} /></button>
                          <span className="w-8 text-center font-semibold">{item.quantity}</span>
                          <button onClick={() => updateQuantity(item.id, 1)} className="p-1 hover:bg-gray-200 rounded"><Plus size={14} /></button>
                        </div>
                      </td>
                      <td className="py-4 text-right text-gray-600">${item.price.toFixed(2)}</td>
                      <td className="py-4 text-right font-semibold text-gray-900">${(item.price * item.quantity).toFixed(2)}</td>
                      <td className="py-4 text-center">
                        <button onClick={() => removeFromCart(item.id)} className="text-red-400 hover:text-red-600 p-1 transition-colors">
                          <Trash2 size={18} />
                        </button>
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>
        </div>

        {/* Controls / Payment */}
        <div className="bg-white rounded-2xl shadow-sm border border-gray-200 p-6 flex flex-col gap-6 h-[70vh]">
          <div className="relative">
            <label className="block text-sm font-medium text-gray-700 mb-2">Search Product</label>
            <div className="relative">
              <input
                type="text"
                value={searchQuery}
                onChange={handleSearch}
                placeholder="Search by ID or Name..."
                className="w-full p-3 pr-10 rounded-lg border border-gray-300 focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none transition-all"
              />
              <div className="absolute right-3 top-3 text-gray-400">
                {isSearching ? <Loader2 className="animate-spin" size={20} /> : '🔍'}
              </div>
            </div>
            {filteredProducts.length > 0 && (
              <div className="absolute z-50 w-full mt-1 bg-white rounded-lg shadow-xl border border-gray-200 overflow-hidden">
                {filteredProducts.map(p => (
                  <div
                    key={p.id}
                    onClick={() => selectProduct(p)}
                    className="p-3 hover:bg-blue-50 cursor-pointer flex justify-between items-center transition-colors border-b last:border-b-0"
                  >
                    <div>
                      <div className="font-medium text-gray-900">{p.name}</div>
                      <div className="text-xs text-gray-500">SKU: {p.sku} | ID: {p.id}</div>
                    </div>
                    <div className="font-semibold text-blue-600">${p.price.toFixed(2)}</div>
                  </div>
                ))}
              </div>
            )}
          </div>

          <div className="space-y-2">
            <label className="block text-sm font-medium text-gray-700">Customer</label>
            <div className="flex gap-2">
              <select
                value={customerId || ''}
                onChange={(e) => setCustomerId(e.target.value)}
                className="flex-1 p-3 rounded-lg border border-gray-300 outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">New Customer</option>
                {customers.map(c => (
                  <option key={c.id} value={c.id}>{c.name} ({c.phone})</option>
                ))}
              </select>
              <button
                onClick={() => setIsCustomerModalOpen(true)}
                className="p-3 bg-gray-100 text-gray-600 rounded-lg hover:bg-gray-200 transition-colors"
                title="Add New Customer"
              >
                <UserPlus size={20} />
              </button>
            </div>
          </div>

          <div className="space-y-3">
            <label className="block text-sm font-medium text-gray-700">Payment Mode</label>
            <div className="grid grid-cols-2 gap-3">
              <button
                onClick={() => setPaymentMode('PAID')}
                className={`flex items-center justify-center gap-2 p-3 rounded-lg border transition-all font-medium ${paymentMode === 'PAID' ? 'bg-blue-600 text-white border-blue-600 shadow-md' : 'bg-white text-gray-600 border-gray-300 hover:bg-gray-50'}`}
              >
                <Banknote size={18} />
                Cash/Paid
              </button>
              <button
                onClick={() => setPaymentMode('CREDIT')}
                className={`flex items-center justify-center gap-2 p-3 rounded-lg border transition-all font-medium ${paymentMode === 'CREDIT' ? 'bg-blue-600 text-white border-blue-600 shadow-md' : 'bg-white text-gray-600 border-gray-300 hover:bg-gray-50'}`}
              >
                <CreditCard size={18} />
                Credit
              </button>
            </div>
          </div>

          <div className="mt-auto space-y-4 border-t pt-6">
            <div className="flex justify-between text-gray-600">
              <span>Subtotal:</span>
              <span className="font-medium">${calculateSubtotal().toFixed(2)}</span>
            </div>
            <div className="flex justify-between items-center text-gray-600">
              <span>Discount ($):</span>
              <input
                type="number"
                value={discount}
                onChange={(e) => setDiscount(parseFloat(e.target.value) || 0)}
                className="w-24 p-1 text-right border rounded outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>
            <div className="flex justify-between text-2xl font-bold text-gray-900 pt-4 border-t">
              <span>Total:</span>
              <span className="text-blue-600">${calculateTotal().toFixed(2)}</span>
            </div>
            {paymentMode === 'PAID' && (
              <div className="space-y-2 animate-in fade-in slide-in-from-top-2 duration-200">
                <label className="block text-sm font-medium text-gray-700">Amount Paid ($)</label>
                <input
                  type="number"
                  value={amountPaid}
                  onChange={(e) => setAmountPaid(e.target.value)}
                  placeholder="0.00"
                  className="w-full p-3 rounded-lg border border-gray-300 outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
            )}
            <button
              onClick={handleCompleteSale}
              disabled={isSubmitting}
              className={`w-full py-4 rounded-xl font-bold text-lg transition-all shadow-lg ${
                isSubmitting ? 'bg-gray-400 cursor-not-allowed' : 'bg-blue-600 text-white hover:bg-blue-700 shadow-blue-200'
              }`}
            >
              {isSubmitting ? 'Processing...' : 'Complete Sale'}
            </button>
          </div>
        </div>

        {/* Quick Add Customer Modal */}
        {isCustomerModalOpen && (
          <div className="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50 backdrop-blur-sm">
            <div className="bg-white rounded-2xl shadow-xl w-full max-w-md overflow-hidden animate-in fade-in zoom-in duration-200">
              <div className="p-6 border-b border-gray-100 flex justify-between items-center">
                <h2 className="text-xl font-bold text-gray-800">Quick Add Customer</h2>
                <button onClick={() => setIsCustomerModalOpen(false)} className="text-gray-400 hover:text-gray-600"><X size={20} /></button>
              </div>
              <form onSubmit={handleAddQuickCustomer} className="p-6 space-y-4">
                <div className="grid grid-cols-1 gap-4">
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">Full Name *</label>
                    <input
                      type="text"
                      required
                      value={newCustomer.name}
                      onChange={(e) => setNewCustomer({...newCustomer, name: e.target.value})}
                      className="w-full p-2 rounded-lg border border-gray-300 focus:ring-2 focus:ring-blue-500 outline-none"
                    />
                  </div>
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">Phone Number *</label>
                    <input
                      type="text"
                      required
                      value={newCustomer.phone}
                      onChange={(e) => setNewCustomer({...newCustomer, phone: e.target.value})}
                      className="w-full p-2 rounded-lg border border-gray-300 focus:ring-2 focus:ring-blue-500 outline-none"
                    />
                  </div>
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">Email (Optional)</label>
                    <input
                      type="email"
                      value={newCustomer.email}
                      onChange={(e) => setNewCustomer({...newCustomer, email: e.target.value})}
                      className="w-full p-2 rounded-lg border border-gray-300 focus:ring-2 focus:ring-blue-500 outline-none"
                    />
                  </div>
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">Address (Optional)</label>
                    <textarea
                      value={newCustomer.address}
                      onChange={(e) => setNewCustomer({...newCustomer, address: e.target.value})}
                      className="w-full p-2 rounded-lg border border-gray-300 focus:ring-2 focus:ring-blue-500 outline-none"
                      rows="3"
                    />
                  </div>
                </div>
                <div className="pt-4 flex justify-end gap-3">
                  <button
                    type="button"
                    onClick={() => setIsCustomerModalOpen(false)}
                    className="px-4 py-2 text-gray-600 hover:bg-gray-100 rounded-lg transition-colors"
                  >
                    Cancel
                  </button>
                  <button
                    type="submit"
                    className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors font-medium"
                  >
                    Save Customer
                  </button>
                </div>
              </form>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default Billing;
