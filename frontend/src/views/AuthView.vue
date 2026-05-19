<script setup>
import { computed, ref } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import api from '../services/api';
import { useAuth } from '../composables/useAuth';

const router = useRouter();
const route = useRoute();
const { setAuth } = useAuth();

const mode = ref('login');
const errorMessage = ref('');
const loading = ref(false);

const loginForm = ref({
  username: '',
  password: '',
});

const registerForm = ref({
  username: '',
  password: '',
  role: 'PASSENGER',
});

const activeForm = computed(() => (mode.value === 'login' ? loginForm.value : registerForm.value));

const canSubmit = computed(() => {
  const form = activeForm.value;
  return form.username && form.password;
});


const handleAuth = async () => {
  errorMessage.value = '';
  loading.value = true;
  let signedRole = '';

  try {
    if (mode.value === 'login') {
      const response = await api.login({ ...loginForm.value });
      setAuth({
        token: response.token,
        userId: response.userId,
        username: response.username,
        role: response.role,
      });
      signedRole = response.role;
    } else {
      const response = await api.register({
        ...registerForm.value,
      });
      setAuth({
        token: response.token,
        userId: response.userId,
        username: response.username,
        role: response.role,
      });
      signedRole = response.role;
    }

    const destination = route.query.redirect;
    if (destination) {
      router.push(destination);
      return;
    }

    if (signedRole === 'PASSENGER') {
      router.push({ name: 'passenger-dashboard' });
    } else {
      router.push({ name: 'management-overview' });
    }
  } catch (err) {
    errorMessage.value = 'Invalid credentials. Please try again.';
  } finally {
    loading.value = false;
  }
};
</script>

<template>
  <section class="px-6 py-16">
    <div class="mx-auto max-w-3xl">
      <div class="rounded-3xl border border-white/10 bg-white/5 p-8 shadow-2xl">
        <p class="text-xs font-semibold uppercase tracking-[0.35em] text-cyan-200">SkyPort Portal</p>
        <h1 class="mt-4 text-3xl font-semibold text-white">Welcome back to the airport command layer.</h1>
        <p class="mt-4 text-sm text-slate-300">Secure access to passenger services, duty-free, and operational control.</p>

        <div class="mt-8 flex gap-3">
          <button
            class="rounded-full px-4 py-2 text-xs font-semibold uppercase tracking-[0.25em]"
            :class="mode === 'login' ? 'bg-white text-slate-900' : 'border border-white/20 text-white'"
            @click="mode = 'login'"
          >
            Login
          </button>
          <button
            class="rounded-full px-4 py-2 text-xs font-semibold uppercase tracking-[0.25em]"
            :class="mode === 'register' ? 'bg-white text-slate-900' : 'border border-white/20 text-white'"
            @click="mode = 'register'"
          >
            Register
          </button>
        </div>

        <form class="mt-8 space-y-4" @submit.prevent="handleAuth">
          <label class="block text-xs font-semibold uppercase tracking-[0.2em] text-slate-300">Username</label>
          <input
            v-model="activeForm.username"
            type="text"
            class="w-full rounded-xl border border-white/10 bg-white/5 px-4 py-3 text-sm text-white outline-none focus:border-cyan-300"
            required
          />

          <label class="block text-xs font-semibold uppercase tracking-[0.2em] text-slate-300">Password</label>
          <input
            v-model="activeForm.password"
            type="password"
            class="w-full rounded-xl border border-white/10 bg-white/5 px-4 py-3 text-sm text-white outline-none focus:border-cyan-300"
            required
          />

          <div v-if="mode === 'register'" class="space-y-2">
            <label class="block text-xs font-semibold uppercase tracking-[0.2em] text-slate-300">Role</label>
            <select
              v-model="registerForm.role"
              class="w-full rounded-xl border border-white/10 bg-slate-950 px-4 py-3 text-sm text-slate-100 outline-none focus:border-cyan-300"
            >
              <option value="PASSENGER">Passenger</option>
              <option value="ADMIN">Admin</option>
            </select>
            <p class="text-xs text-slate-400">Admin access should be used only for internal testing.</p>
          </div>

          <p v-if="errorMessage" class="rounded-xl border border-red-500/30 bg-red-500/10 px-4 py-2 text-sm text-red-200">
            {{ errorMessage }}
          </p>

          <button
            type="submit"
            class="mt-4 w-full rounded-xl bg-white px-5 py-3 text-sm font-semibold text-slate-900 transition hover:bg-slate-100 disabled:opacity-60"
            :disabled="!canSubmit || loading"
          >
            {{ loading ? 'Please wait...' : mode === 'login' ? 'Sign In' : 'Create Account' }}
          </button>
        </form>
      </div>
    </div>
  </section>
</template>
