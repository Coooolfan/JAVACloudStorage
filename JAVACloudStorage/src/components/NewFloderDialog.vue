<script setup lang="ts">
import { postFolder } from "@/api/FileMap";
import { inject, ref } from "vue";
import FloatLabel from "primevue/floatlabel";
import InputText from "primevue/inputtext";
import Button from "primevue/button";
import { useToast } from "primevue/usetoast";

const newFolderName = ref("");
const toast = useToast();
const dialogRef: any = inject("dialogRef");

async function createNewFloder() {
  const resp = await postFolder(newFolderName.value, dialogRef.value.data.dirId);
  if (resp.length === 0) {
    toast.add({
      severity: "error",
      summary: "创建失败",
      detail: `文件夹${newFolderName.value}创建失败`,
      life: 3000,
    });
    return;
  }
  toast.add({
    severity: "success",
    summary: "创建成功",
    detail: `文件夹${newFolderName.value}创建成功`,
    life: 3000,
  });
  closeDialog();
}


const closeDialog = () => {
  dialogRef.value.close();
};
</script>

<template>
  <div class="flex items-center mb-4 mt-6">
    <FloatLabel>
      <InputText id="username" v-model="newFolderName" class="w-64" />
      <label for="username">文件夹名</label>
    </FloatLabel>
  </div>
  <div class="flex justify-end gap-2">
    <Button
      type="button"
      label="取消"
      severity="secondary"
      @click="closeDialog"
    ></Button>
    <Button type="button" label="创建" @click="createNewFloder"></Button>
  </div>
</template>
