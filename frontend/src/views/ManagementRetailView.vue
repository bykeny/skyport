<script setup>
import { onMounted, ref } from 'vue';
import api from '../services/api';
import SectionCard from '../components/ui/SectionCard.vue';
import InlineAlert from '../components/ui/InlineAlert.vue';
import StatusBadge from '../components/ui/StatusBadge.vue';

const products = ref([]);
const orders = ref([]);
const loading = ref(false);
const errorMessage = ref('');
const orderFlightId = ref('');
const productForm = ref({ sku: '', name: '', category: '', price: '', stockQuantity: '' });
const orderForm = ref({ passengerId: '', flightId: '', productId: '', quantity: 1 });

const loadRetail = async () => {
  loading.value = true;
  errorMessage.value = '';
  try {
    products.value = await api.getProducts();
    if (orderFlightId.value) {
      orders.value = await api.getRetailOrdersByFlight(orderFlightId.value);
    }
  } catch (err) {
    errorMessage.value = 'Unable to load retail inventory.';
    products.value = [];
  } finally {
    loading.value = false;
  }
};

const createProduct = async () => {
  loading.value = true;
  errorMessage.value = '';
  try {
    await api.createProduct({
      sku: productForm.value.sku,
      name: productForm.value.name,
      category: productForm.value.category,
      price: Number(productForm.value.price),
      stockQuantity: Number(productForm.value.stockQuantity || 0),
    });
    productForm.value = { sku: '', name: '', category: '', price: '', stockQuantity: '' };
    await loadRetail();
  } catch (err) {
    errorMessage.value = 'Unable to create product.';
  } finally {
    loading.value = false;
  }
};

const createOrder = async () => {
  loading.value = true;
  errorMessage.value = '';
  try {
    await api.createRetailOrder({
      passengerId: Number(orderForm.value.passengerId),
      flightId: Number(orderForm.value.flightId),
      productId: Number(orderForm.value.productId),
      quantity: Number(orderForm.value.quantity || 1),
    });
    orderForm.value = { passengerId: '', flightId: '', productId: '', quantity: 1 };
    await loadRetail();
  } catch (err) {
    errorMessage.value = 'Unable to create retail order.';
  } finally {
    loading.value = false;
  }
};

onMounted(loadRetail);
</script>

<template>
  <div class="space-y-6">
    <SectionCard title="Retail & Inventory" subtitle="Duty-free catalog and order management.">
      <InlineAlert
        v-if="errorMessage"
        tone="warning"
        :message="errorMessage"
        action-label="Retry"
        @action="loadRetail"
      />

      <div class="grid gap-4 md:grid-cols-2">
        <div class="rounded-xl border border-white/10 bg-white/5 p-4">
          <p class="text-sm font-semibold text-white">Create Product</p>
          <div class="mt-3 grid gap-3">
            <input v-model="productForm.sku" class="rounded-lg border border-white/10 bg-slate-950 px-3 py-2 text-sm text-white" placeholder="SKU" />
            <input v-model="productForm.name" class="rounded-lg border border-white/10 bg-slate-950 px-3 py-2 text-sm text-white" placeholder="Name" />
            <input v-model="productForm.category" class="rounded-lg border border-white/10 bg-slate-950 px-3 py-2 text-sm text-white" placeholder="Category" />
            <input v-model="productForm.price" type="number" min="0" step="0.01" class="rounded-lg border border-white/10 bg-slate-950 px-3 py-2 text-sm text-white" placeholder="Price" />
            <input v-model="productForm.stockQuantity" type="number" min="0" class="rounded-lg border border-white/10 bg-slate-950 px-3 py-2 text-sm text-white" placeholder="Stock Quantity" />
            <button class="rounded-full bg-white px-4 py-2 text-sm font-semibold text-slate-900" @click="createProduct">
              Save Product
            </button>
          </div>
        </div>
        <div class="rounded-xl border border-white/10 bg-white/5 p-4">
          <p class="text-sm font-semibold text-white">Create Order</p>
          <div class="mt-3 grid gap-3">
            <input v-model="orderForm.passengerId" class="rounded-lg border border-white/10 bg-slate-950 px-3 py-2 text-sm text-white" placeholder="Passenger ID" />
            <input v-model="orderForm.flightId" class="rounded-lg border border-white/10 bg-slate-950 px-3 py-2 text-sm text-white" placeholder="Flight ID" />
            <input v-model="orderForm.productId" class="rounded-lg border border-white/10 bg-slate-950 px-3 py-2 text-sm text-white" placeholder="Product ID" />
            <input v-model="orderForm.quantity" type="number" min="1" class="rounded-lg border border-white/10 bg-slate-950 px-3 py-2 text-sm text-white" placeholder="Quantity" />
            <button class="rounded-full bg-white px-4 py-2 text-sm font-semibold text-slate-900" @click="createOrder">
              Create Order
            </button>
          </div>
        </div>
      </div>

      <div class="mt-6 grid gap-4 md:grid-cols-2">
        <div class="rounded-xl border border-white/10">
          <div class="border-b border-white/10 px-4 py-3 text-sm font-semibold text-white">Products</div>
          <div class="divide-y divide-white/10">
            <div v-if="!products.length" class="px-4 py-3 text-sm text-slate-300">No products available.</div>
            <div v-for="product in products" :key="product.id" class="px-4 py-3 text-sm">
              <p class="font-semibold text-white">{{ product.name }}</p>
              <p class="text-xs text-slate-400">{{ product.category }} · Stock {{ product.stockQuantity }}</p>
            </div>
          </div>
        </div>

        <div class="rounded-xl border border-white/10">
          <div class="flex items-center justify-between border-b border-white/10 px-4 py-3">
            <p class="text-sm font-semibold text-white">Orders by Flight</p>
            <input v-model="orderFlightId" class="w-28 rounded-lg border border-white/10 bg-slate-950 px-2 py-1 text-xs text-white" placeholder="Flight ID" />
            <button class="rounded-full border border-white/20 px-3 py-1 text-xs font-semibold text-slate-100" @click="loadRetail">
              Load
            </button>
          </div>
          <div class="divide-y divide-white/10">
            <div v-if="!orders.length" class="px-4 py-3 text-sm text-slate-300">No orders loaded.</div>
            <div v-for="order in orders" :key="order.id" class="px-4 py-3 text-sm">
              <div class="flex items-center justify-between">
                <p class="font-semibold text-white">{{ order.productName || 'Order' }}</p>
                <StatusBadge :status="order.status" />
              </div>
              <p class="text-xs text-slate-400">Passenger {{ order.passengerId }} · Qty {{ order.quantity }}</p>
            </div>
          </div>
        </div>
      </div>
    </SectionCard>
  </div>
</template>
