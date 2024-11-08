<script setup lang="ts">
import { RouterLink, RouterView } from "vue-router";
import TopMenu from "@/components/TopMenu.vue";
</script>

<template>
  <TopMenu></TopMenu>

  <RouterView />
</template>
