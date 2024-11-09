<script setup lang="ts">
import Menubar from "primevue/menubar";
import InputText from "primevue/inputtext";
import Avatar from "primevue/avatar";
import { inject, onMounted, ref } from "vue";
import { defaultUser, getUser, type User } from "@/api/User";
import logo from "@/assets/logo.jpg";

const user = ref<User>(structuredClone(defaultUser));

onMounted(async () => {
  user.value = await getUser();
  localStorage.setItem("usedQuota", user.value.usedQuota.toString());
  localStorage.setItem("quota", user.value.quota.toString());
});
const items = ref();
function jump2Home() {
  window.location.href = "/dashboard/-1";
}
</script>
<template>
  <Menubar :model="items">
    <template #start>
      <Avatar :image="logo" shape="circle" class="ml-2" />
      <h1 class="font-bold ml-4 mr-4 cursor-pointer" @click="jump2Home">
        JAVA Cloud Storage
      </h1>
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
