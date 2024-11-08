<script setup lang="ts">
import Menubar from "primevue/menubar";
import InputText from "primevue/inputtext";
import Avatar from "primevue/avatar";
import { onMounted, ref } from "vue";
import { defaultUser, getUser } from "@/api/User";
import logo from "@/assets/logo.jpg";

const user = ref(structuredClone(defaultUser));
onMounted(async () => {
  user.value = await getUser();
});
const items = ref([
  {
    label: "主页",
    icon: "pi pi-home",
  },
]);
</script>
<template>
    <Menubar :model="items">
      <template #start>
        <Avatar :image="logo" shape="circle" class="ml-4 mr-8" />
      </template>
      <template #item="{ item, props, hasSubmenu, root }">
        <a v-ripple class="flex items-center" v-bind="props.action">
          <i :class="item.icon"></i>
          <span>{{ item.label }}</span>
        </a>
      </template>
      <template #end>
        <div class="flex items-center gap-4">
          <InputText placeholder="搜索" type="text" class="w-32 sm:w-auto" />
          <span>{{ user.username }}</span>
          <Avatar :image="user.avatar" shape="circle" />
        </div>
      </template>
    </Menubar>
</template>
