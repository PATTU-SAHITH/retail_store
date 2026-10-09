import { create } from 'zustand';

export const useCartStore = create((set) => ({
  cart: [],
  customerId: null,
  discount: 0,

  setCustomerId: (id) => set({ customerId: id }),
  setDiscount: (value) => set({ discount: value }),

  addToCart: (product) => set((state) => {
    const existing = state.cart.find(item => item.id === product.id);
    if (existing) {
      return {
        cart: state.cart.map(item =>
          item.id === product.id
            ? { ...item, quantity: item.quantity + 1 }
            : item
        )
      };
    }
    return { cart: [...state.cart, { ...product, quantity: 1 }] };
  }),

  updateQuantity: (productId, delta) => set((state) => ({
    cart: state.cart.map(item =>
      item.id === productId
        ? { ...item, quantity: Math.max(1, item.quantity + delta) }
        : item
    )
  })),

  removeFromCart: (productId) => set((state) => ({
    cart: state.cart.filter(item => item.id !== productId)
  })),

  clearCart: () => set({ cart: [], customerId: null, discount: 0 }),
}));
