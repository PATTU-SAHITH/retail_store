import React, { useState, useEffect } from 'react';
import { User, History, ArrowUpRight, DollarSign, Plus, Edit2, X } from 'lucide-react';
import { customerApi, financialApi } from '../api/api';

const Credit = () => {
  const [customers, setCustomers] = useState([]);
  const [selectedCustomer, setSelectedCustomer] = useState(null);
  const [ledger, setLedger] = useState([]);
  const [paymentAmount, setPaymentAmount] = useState('');
  const [loading, setLoading] = useState(false);

  // Modal states
  const [isCustomerModalOpen, setIsCustomerModalOpen] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  const [customerFormData, setCustomerFormData] = useState({ name: '', phone: '', email: '', address: '' });

  useEffect(() => {
    loadCustomers();
  }, []);

  const loadCustomers = async () => {
    try {
      const res = await customerApi.getCustomers();
      setCustomers(res.data);
    } catch (error) {
      console.error("Failed to load customers", error);
    }
  };

  const selectCustomer = async (customer) => {
    setSelectedCustomer(customer);
    try {
      const res = await financialApi.getCustomerLedger(customer.id);
      setLedger(res.data);
    } catch (error) {
      console.error("Failed to load ledger", error);
    }
  };

  const handlePayment = async () => {
    if (!selectedCustomer || !paymentAmount) return;

    setLoading(true);
    try {
      await financialApi.makePayment({
        customerId: selectedCustomer.id,
        amount: parseFloat(paymentAmount),
        date: new Date().toISOString()
      });
      setPaymentAmount('');
      await selectCustomer(selectedCustomer); // Refresh ledger
      alert("Payment recorded successfully!");
    } catch (error) {
      alert("Error recording payment");
    } finally {
      setLoading(false);
    }
  };

  const handleSaveCustomer = async (e) => {
    e.preventDefault();
    if (!customerFormData.phone) {
      alert("Phone number is mandatory!");
      return;
    }

    try {
      if (isEditing) {
        await customerApi.updateCustomer(selectedCustomer.id, customerFormData);
      } else {
        await customerApi.createCustomer(customerFormData);
      }
      setIsCustomerModalOpen(false);
      setIsEditing(false);
      setCustomerFormData({ name: '', phone: '', email: '', address: '' });
      loadCustomers();
      if (isEditing) {
        await selectCustomer(selectedCustomer); // Refresh selected customer info
      }
    } catch (error) {
      alert("Error saving customer: " + (error.response?.data?.message || error.message));
    }
  };

  const openAddModal = () => {
    setIsEditing(false);
    setCustomerFormData({ name: '', phone: '', email: '', address: '' });
    setIsCustomerModalOpen(true);
  };

  const openEditModal = (customer) => {
    setIsEditing(true);
    setSelectedCustomer(customer);
    setCustomerFormData({
      name: customer.name,
      phone: customer.phone,
      email: customer.email || '',
      address: customer.address || ''
    });
    setIsCustomerModalOpen(true);
  };

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-3xl font-bold text-gray-800">Customer Credit</h1>
        <div className="flex gap-3">
          <button
            onClick={openAddModal}
            className="flex items-center gap-2 px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors font-medium shadow-sm"
          >
            <Plus size={20} />
            Add Customer
          </button>
          <button
            onClick={handlePayment}
            disabled={!selectedCustomer || loading}
            className="flex items-center gap-2 px-4 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 transition-colors font-medium disabled:bg-gray-400"
          >
            <ArrowUpRight size={20} />
            {loading ? 'Processing...' : 'Record Payment'}
          </button>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Customer Debt List */}
        <div className="lg:col-span-1 bg-white rounded-2xl shadow-sm border border-gray-200 overflow-hidden flex flex-col h-[70vh]">
          <div className="p-4 bg-gray-50 border-b border-gray-200 font-semibold text-gray-700">
            Customer List
          </div>
          <div className="overflow-y-auto flex-1 divide-y divide-gray-100">
            {customers.length === 0 ? (
              <div className="p-8 text-center text-gray-400 italic">No customers found.</div>
            ) : (
              customers.map(c => (
                <div
                  key={c.id}
                  onClick={() => selectCustomer(c)}
                  className={`p-4 cursor-pointer transition-colors flex justify-between items-center ${selectedCustomer?.id === c.id ? 'bg-blue-50 border-l-4 border-blue-600' : 'hover:bg-gray-50'}`}
                >
                  <div className="flex-1">
                    <div className="font-medium text-gray-900">{c.name}</div>
                    <div className="text-xs text-gray-500">{c.phone}</div>
                  </div>
                  <div className="flex items-center gap-3">
                    <button
                      onClick={(e) => { e.stopPropagation(); openEditModal(c); }}
                      className="p-2 text-gray-400 hover:text-blue-600 transition-colors"
                    >
                      <Edit2 size={16} />
                    </button>
                    <div className="text-right">
                      <div className="text-xs text-gray-400 uppercase">Balance</div>
                      <div className="font-bold text-gray-900">$0.00</div>
                    </div>
                  </div>
                </div>
              ))
            )}
          </div>
        </div>

        {/* Detailed Ledger View */}
        <div className="lg:col-span-2 bg-white rounded-2xl shadow-sm border border-gray-200 p-6 h-[70vh] flex flex-col">
          <div className="flex justify-between items-center mb-6">
            <div className="flex items-center gap-3">
              <User className="text-gray-400" size={24} />
              <h2 className="text-xl font-bold text-gray-800">
                {selectedCustomer ? `${selectedCustomer.name}'s Ledger` : 'Customer Transaction History'}
              </h2>
            </div>
            {selectedCustomer && (
              <div className="flex items-center gap-2">
                <span className="text-sm text-gray-500">Payment:</span>
                <input
                  type="number"
                  value={paymentAmount}
                  onChange={(e) => setPaymentAmount(e.target.value)}
                  className="p-2 border rounded-lg w-32 text-right outline-none focus:ring-2 focus:ring-blue-500"
                  placeholder="Amount $"
                />
              </div>
            )}
          </div>

          <div className="flex-1 overflow-auto">
            {!selectedCustomer ? (
              <div className="h-full flex flex-col items-center justify-center text-gray-400 italic text-center p-10 bg-gray-50 rounded-xl border border-dashed border-gray-300">
                <History size={48} className="mb-4 opacity-20" />
                Select a customer from the list to view their credit history.
              </div>
            ) : (
              <table className="w-full text-left">
                <thead className="text-gray-400 text-sm uppercase tracking-wider border-b border-gray-100">
                  <tr>
                    <th className="pb-3 font-medium">Date</th>
                    <th className="pb-3 font-medium">Description</th>
                    <th className="pb-3 font-medium text-right">Amount</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-gray-50">
                  {ledger.length === 0 ? (
                    <tr>
                      <td colSpan="3" className="py-10 text-center text-gray-400 italic">No transactions found.</td>
                    </tr>
                  ) : (
                    ledger.map((entry, idx) => (
                      <tr key={idx} className="hover:bg-gray-50 transition-colors">
                        <td className="py-4 text-sm text-gray-600">{new Date(entry.date).toLocaleDateString()}</td>
                        <td className="py-4 text-gray-700">{entry.description}</td>
                        <td className={`py-4 text-right font-semibold ${entry.amount > 0 ? 'text-red-600' : 'text-green-600'}`}>
                          {entry.amount > 0 ? `+$${entry.amount.toFixed(2)}` : `-$${Math.abs(entry.amount).toFixed(2)}`}
                        </td>
                      </tr>
                    ))
                  )}
                </tbody>
              </table>
            )}
          </div>
        </div>
      </div>

      {/* Customer Modal */}
      {isCustomerModalOpen && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50 backdrop-blur-sm">
          <div className="bg-white rounded-2xl shadow-xl w-full max-w-md overflow-hidden animate-in fade-in zoom-in duration-200">
            <div className="p-6 border-b border-gray-100 flex justify-between items-center">
              <h2 className="text-xl font-bold text-gray-800">{isEditing ? 'Edit Customer' : 'Add New Customer'}</h2>
              <button onClick={() => setIsCustomerModalOpen(false)} className="text-gray-400 hover:text-gray-600"><X size={20} /></button>
            </div>
            <form onSubmit={handleSaveCustomer} className="p-6 space-y-4">
              <div className="grid grid-cols-1 gap-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Full Name</label>
                  <input
                    type="text"
                    required
                    value={customerFormData.name}
                    onChange={(e) => setCustomerFormData({...customerFormData, name: e.target.value})}
                    className="w-full p-2 rounded-lg border border-gray-300 focus:ring-2 focus:ring-blue-500 outline-none"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Phone Number *</label>
                  <input
                    type="text"
                    required
                    value={customerFormData.phone}
                    onChange={(e) => setCustomerFormData({...customerFormData, phone: e.target.value})}
                    className="w-full p-2 rounded-lg border border-gray-300 focus:ring-2 focus:ring-blue-500 outline-none"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Email (Optional)</label>
                  <input
                    type="email"
                    value={customerFormData.email}
                    onChange={(e) => setCustomerFormData({...customerFormData, email: e.target.value})}
                    className="w-full p-2 rounded-lg border border-gray-300 focus:ring-2 focus:ring-blue-500 outline-none"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Address (Optional)</label>
                  <textarea
                    value={customerFormData.address}
                    onChange={(e) => setCustomerFormData({...customerFormData, address: e.target.value})}
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
                  {isEditing ? 'Update Customer' : 'Save Customer'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default Credit;
