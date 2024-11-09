<script setup lang="ts">
import { RouterLink, RouterView } from "vue-router";
import TopMenu from "@/components/TopMenu.vue";
import DynamicDialog from "primevue/dynamicdialog";
import LeftMenu from "@/components/LeftMenu.vue";
import Toast from "primevue/toast";

</script>

<template>
  <Toast position="bottom-right" />
  <DynamicDialog />
  <div class="h-screen">
    <TopMenu class="h-[9%]"></TopMenu>
    <div class="flex h-[91%]">
      <LeftMenu class="w-1/6"></LeftMenu>
      <RouterView class="w-5/6" />
    </div>
  </div>
</template>
