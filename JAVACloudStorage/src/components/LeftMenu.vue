<script setup lang="ts">
import type { User } from "@/api/User";
import { computed, inject, onMounted, ref } from "vue";
import ProgressBar from "primevue/progressbar";
const usedQuota = ref(0);
import { Button } from "primevue";
const Quota = ref(100);

onMounted(async () => {
  usedQuota.value = Number(localStorage.getItem("usedQuota"));
  Quota.value = Number(localStorage.getItem("quota"));
});

const usaged = computed(() => {
  // 整数
  return Math.floor((usedQuota.value / Quota.value) * 100);
});

const unusaged = computed(() => {
  // 整数
  return 100 - Math.floor((usedQuota.value / Quota.value) * 100);
});

const beautySize = (size: number) => {
  if (size < 1024) {
    return size + " KB";
  } else if (size < 1024 * 1024) {
    return (size / 1024).toFixed(2) + " MB";
  } else {
    return (size / 1024 / 1024).toFixed(2) + " GB";
  }
};

function clearAllCookies() {
  const cookies = document.cookie.split(";");

  for (let cookie of cookies) {
    const eqPos = cookie.indexOf("=");
    const name = eqPos > -1 ? cookie.substr(0, eqPos) : cookie;
    document.cookie = `${name}=;expires=Thu, 01 Jan 1970 00:00:00 GMT;path=/`;
  }
}

function logout() {
  // 清空cookie
  clearAllCookies();
  window.location.href = "/login";
}
</script>
<template>
  <div class="bg-slate-100 flex justify-between flex-col">
    <div class="mt-4 ml-6 flex flex-col gap-4">
      <div class="flex gap-4 items-center cursor-pointer">
        <i class="pi pi-heart" style="font-size: 1.25rem"></i>
        <span class="text-lg font-b">收藏</span>
      </div>
      <div class="flex gap-4 items-center cursor-pointer">
        <i class="pi pi-file-word" style="font-size: 1.25rem"></i>
        <span class="text-lg font-b">文档</span>
      </div>
      <div class="flex gap-4 items-center cursor-pointer">
        <i class="pi pi-image" style="font-size: 1.25rem"></i>
        <span class="text-lg font-b">图片</span>
      </div>
      <div class="flex gap-4 items-center cursor-pointer">
        <i class="pi pi-video" style="font-size: 1.25rem"></i>
        <span class="text-lg font-b">视频</span>
      </div>
    </div>

    <div class="flex flex-col">
      <Button
        label="退出登录"
        class="mb-2 ml-4 mr-4 h-8"
        severity="secondary"
        @click="logout"
      ></Button>
      <ProgressBar :value="usaged" class="ml-4 mr-4"></ProgressBar>
      <div class="flex justify-between ml-2 mr-2 mb-2 mt-1">
        <span class="text-xs">{{ beautySize(usedQuota) }}</span>
        <span class="text-xs">{{ beautySize(Quota) }}</span>
      </div>
    </div>
  </div>
</template>
