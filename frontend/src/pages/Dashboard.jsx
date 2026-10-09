import React, { useState, useEffect } from 'react';
import { TrendingUp, TrendingDown, AlertTriangle, DollarSign } from 'lucide-react';
import { catalogApi, storeApi } from '../api/api';
import api from '../api/api';

const Dashboard = () => {
  const [summary, setSummary] = useState({
    todayRevenue: 0,
    totalBills: 0,
    lowStockCount: 0,
    totalDebt: 0
  });
  const [mostSold, setMostSold] = useState([]);
  const [leastMoving, setLeastMoving] = useState([]);
  const [lowStock, setLowStock] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadDashboardData();
  }, []);

  const loadDashboardData = async () => {
    try {
      const [summaryRes, mostSoldRes, leastMovingRes, lowStockRes] = await Promise.all([
        api.get('/dashboard/summary'),
        api.get('/dashboard/most-sold'),
        api.get('/dashboard/least-moving'),
        storeApi.getLowStockItems()
      ]);

      setSummary(summaryRes.data);
      setMostSold(mostSoldRes.data);
      setLeastMoving(leastMovingRes.data);
      setLowStock(lowStockRes.data);
    } catch (error) {
      console.error("Failed to load dashboard data", error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <div className="flex items-center justify-center h-full text-gray-500">Loading Dashboard...</div>;

  return (
    <div className="space-y-8">
      <div className="flex justify-between items-center">
        <h1 className="text-3xl font-bold text-gray-800">Dashboard</h1>
        <button onClick={loadDashboardData} className="px-4 py-2 bg-white border border-gray-200 rounded-lg hover:bg-gray-50 transition-colors text-sm font-medium">
          Refresh Data
        </button>
      </div>

      {/* KPI Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <div className="bg-white p-6 rounded-2xl shadow-sm border border-gray-200">
          <div className="flex items-center justify-between mb-4">
            <div className="p-2 bg-blue-100 text-blue-600 rounded-lg">
              <DollarSign size={24} />
            </div>
          </div>
          <div className="text-gray-500 text-sm">Today's Revenue</div>
          <div className="text-2xl font-bold text-gray-900">${summary.todayRevenue?.toFixed(2) || '0.00'}</div>
        </div>

        <div className="bg-white p-6 rounded-2xl shadow-sm border border-gray-200">
          <div className="flex items-center justify-between mb-4">
            <div className="p-2 bg-purple-100 text-purple-600 rounded-lg">
              <TrendingUp size={24} />
            </div>
          </div>
          <div className="text-gray-500 text-sm">Total Bills</div>
          <div className="text-2xl font-bold text-gray-900">{summary.totalBills || 0}</div>
        </div>

        <div className="bg-white p-6 rounded-2xl shadow-sm border border-gray-200">
          <div className="flex items-center justify-between mb-4">
            <div className="p-2 bg-orange-100 text-orange-600 rounded-lg">
              <AlertTriangle size={24} />
            </div>
          </div>
          <div className="text-gray-500 text-sm">Low Stock Items</div>
          <div className="text-2xl font-bold text-gray-900">{summary.lowStockCount || 0}</div>
        </div>

        <div className="bg-white p-6 rounded-2xl shadow-sm border border-gray-200">
          <div className="flex items-center justify-between mb-4">
            <div className="p-2 bg-red-100 text-red-600 rounded-lg">
              <TrendingDown size={24} />
            </div>
          </div>
          <div className="text-gray-500 text-sm">Outstanding Debt</div>
          <div className="text-2xl font-bold text-gray-900">${summary.totalDebt?.toFixed(2) || '0.00'}</div>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Most Sold Products */}
        <div className="bg-white p-6 rounded-2xl shadow-sm border border-gray-200">
          <h2 className="text-xl font-bold text-gray-800 mb-4 flex items-center gap-2">
            <TrendingUp size={20} className="text-green-500" /> Most Sold
          </h2>
          <div className="space-y-3">
            {mostSold.length === 0 ? (
              <div className="text-gray-400 italic text-center py-4">No sales data available.</div>
            ) : (
              mostSold.map((item, idx) => (
                <div key={idx} className="flex justify-between items-center p-3 bg-gray-50 rounded-lg">
                  <span className="text-gray-700 font-medium">Product #{item.productId}</span>
                  <span className="font-bold text-blue-600">{item.quantity} units</span>
                </div>
              ))
            )}
          </div>
        </div>

        {/* Least Moving Products */}
        <div className="bg-white p-6 rounded-2xl shadow-sm border border-gray-200">
          <h2 className="text-xl font-bold text-gray-800 mb-4 flex items-center gap-2">
            <TrendingDown size={20} className="text-red-500" /> Least Moving
          </h2>
          <div className="space-y-3">
            {leastMoving.length === 0 ? (
              <div className="text-gray-400 italic text-center py-4">No sales data available.</div>
            ) : (
              leastMoving.map((item, idx) => (
                <div key={idx} className="flex justify-between items-center p-3 bg-gray-50 rounded-lg">
                  <span className="text-gray-700 font-medium">Product #{item.productId}</span>
                  <span className="font-bold text-red-600">{item.quantity} units</span>
                </div>
              ))
            )}
          </div>
        </div>

        {/* Low Inventory Alert */}
        <div className="bg-white p-6 rounded-2xl shadow-sm border border-gray-200">
          <h2 className="text-xl font-bold text-gray-800 mb-4 flex items-center gap-2">
            <AlertTriangle size={20} className="text-orange-500" /> Low Stock Alert
          </h2>
          <div className="space-y-3">
            {lowStock.length === 0 ? (
              <div className="text-gray-400 italic text-center py-4">All stock levels are healthy.</div>
            ) : (
              lowStock.map((item, idx) => (
                <div key={idx} className="flex justify-between items-center p-3 bg-red-50 rounded-lg">
                  <span className="text-gray-700 font-medium">{item.product?.name || `Prod #${item.product?.id}`}</span>
                  <span className="font-bold text-red-600">{item.quantity} left</span>
                </div>
              ))
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
