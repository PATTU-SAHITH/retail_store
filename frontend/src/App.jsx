import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import { LayoutDashboard, ShoppingCart, Package, CreditCard } from 'lucide-react';
import Billing from './pages/Billing';
import Dashboard from './pages/Dashboard';
import Catalog from './pages/Catalog';
import Credit from './pages/Credit';

const App = () => {
  return (
    <Router>
      <div className="flex h-screen bg-gray-100 text-gray-900">
        {/* Sidebar */}
        <aside className="w-64 bg-slate-900 text-white flex flex-col">
          <div className="p-6 text-2xl font-bold border-b border-slate-800">
            Retail Store
          </div>
          <nav className="flex-1 p-4 space-y-2">
            <Link to="/" className="flex items-center p-3 rounded-lg hover:bg-slate-800 transition-colors gap-3">
              <ShoppingCart size={20} />
              <span>Billing</span>
            </Link>
            <Link to="/dashboard" className="flex items-center p-3 rounded-lg hover:bg-slate-800 transition-colors gap-3">
              <LayoutDashboard size={20} />
              <span>Dashboard</span>
            </Link>
            <Link to="/catalog" className="flex items-center p-3 rounded-lg hover:bg-slate-800 transition-colors gap-3">
              <Package size={20} />
              <span>Catalog</span>
            </Link>
            <Link to="/credit" className="flex items-center p-3 rounded-lg hover:bg-slate-800 transition-colors gap-3">
              <CreditCard size={20} />
              <span>Credit Page</span>
            </Link>
          </nav>
          <div className="p-4 text-xs text-slate-500 border-t border-slate-800">
            v1.0.0 - Store Manager
          </div>
        </aside>

        {/* Main Content */}
        <main className="flex-1 overflow-auto p-8">
          <Routes>
            <Route path="/" element={<Billing />} />
            <Route path="/dashboard" element={<Dashboard />} />
            <Route path="/catalog" element={<Catalog />} />
            <Route path="/credit" element={<Credit />} />
          </Routes>
        </main>
      </div>
    </Router>
  );
};

export default App;
