import { computed, reactive } from 'vue';

const STORAGE_KEY = 'ams_auth';

const state = reactive({
  token: null,
  userId: null,
  username: '',
  role: '',
});

const readStorage = () => {
  try {
    const raw = localStorage.getItem(STORAGE_KEY);
    return raw ? JSON.parse(raw) : null;
  } catch (err) {
    return null;
  }
};

const writeStorage = (payload) => {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(payload));
};

const clearStorage = () => {
  localStorage.removeItem(STORAGE_KEY);
};

const syncFromStorage = () => {
  const stored = readStorage();
  state.token = stored?.token || null;
  state.userId = stored?.userId ?? null;
  state.username = stored?.username || '';
  state.role = stored?.role || '';
};

const setAuth = (payload) => {
  const next = {
    token: payload?.token || null,
    userId: payload?.userId ?? null,
    username: payload?.username || '',
    role: payload?.role || '',
  };
  writeStorage(next);
  Object.assign(state, next);
};

const clearAuth = () => {
  clearStorage();
  Object.assign(state, {
    token: null,
    userId: null,
    username: '',
    role: '',
  });
};

const isAuthenticated = computed(() => Boolean(state.token));

syncFromStorage();

export const useAuth = () => ({
  state,
  isAuthenticated,
  syncFromStorage,
  setAuth,
  clearAuth,
});

export const getStoredAuth = () => readStorage();
