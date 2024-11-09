<script setup lang="ts">
import { patchFile, postFolder } from "@/api/FileMap";
import { inject, onMounted, ref } from "vue";
import FloatLabel from "primevue/floatlabel";
import InputText from "primevue/inputtext";
import Button from "primevue/button";
import { useToast } from "primevue/usetoast";
const dialogRef: any = inject("dialogRef");

const newName = ref("");
const fileMapId = ref(0);
const toast = useToast();

onMounted(() => {
  newName.value = dialogRef.value.data.filename;
  fileMapId.value = dialogRef.value.data.id;
});

async function patchFilename() {
  const resp = await patchFile(fileMapId.value, newName.value);
  if (!resp) {
    toast.add({
      severity: "error",
      summary: "编辑失败",
      detail: `重命名失败`,
      life: 3000,
    });
    return;
  }
  toast.add({
    severity: "success",
    summary: "编辑成功",
    detail: `重命名成功，刷新页面可见`,
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
      <InputText id="username" v-model="newName" class="w-64" />
      <label for="username">新名字</label>
    </FloatLabel>
  </div>
  <div class="flex justify-end gap-2">
    <Button
      type="button"
      label="取消"
      severity="secondary"
      @click="closeDialog"
    ></Button>
    <Button type="button" label="确定" @click="patchFilename"></Button>
  </div>
</template>
