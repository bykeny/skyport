<script setup>
import { computed } from 'vue'
import { RouterView } from 'vue-router'
import AppLayout from './layouts/AppLayout.vue'
import PublicLayout from './layouts/PublicLayout.vue'

const layoutFor = computed(() => (routeMeta) => {
  if (routeMeta?.layout === 'app') return AppLayout
  return PublicLayout
})
</script>

<template>
  <RouterView v-slot="{ Component, route }">
    <component :is="layoutFor(route.meta)">
      <component :is="Component" />
    </component>
  </RouterView>
</template>

<style>
/* Base styles cleared, handled by Tailwind */
</style>
