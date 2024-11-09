<script setup lang="ts">
import { postNewFile } from "@/api/FileMap";
import Button from "primevue/button";
import FileUpload, { type FileUploadSelectEvent } from "primevue/fileupload";
import { computed, inject, ref } from "vue";
import { useToast } from "primevue/usetoast";
const toast = useToast();
const closeDialog = () => {
  dialogRef.value.close();
};

const newFile = ref({
  file: null,
  file_name: "",
  format: "",
  processing: 0,
});

const dialogRef: any = inject("dialogRef");

async function uploadHandler() {
  const resp = await postNewFile(newFile.value, dialogRef.value.data.dirId);
  if (resp === "linked") {
    toast.add({
      severity: "success",
      summary: "上传完成",
      detail: `秒传成功, 刷新页面可见`,
      life: 3000,
    });
    // 刷新页面
    closeDialog();
    return;
  }
  if (resp === "success") {
    toast.add({
      severity: "success",
      summary: "上传成功",
      detail: `文件上传成功, 刷新页面可见`,
      life: 3000,
    });
    // 刷新页面
    closeDialog();
    return;
  }
  toast.add({
    severity: "error",
    summary: "上传失败",
    detail: `文件上传失败, ${resp}`,
    life: 3000,
  });
  closeDialog();
}

function onFileChooseHandler(e: FileUploadSelectEvent) {
  newFile.value.file = e.files[0];
  const filename = e.files[0].name;
  const name = filename.substring(0, filename.lastIndexOf("."));
  const format = filename.substring(filename.lastIndexOf(".") + 1);
  newFile.value.file_name = name;
  newFile.value.format = format;
}

const submitText = computed(() => {
  return newFile.value.processing !== 0
    ? newFile.value.processing + "%"
    : "上传";
});
</script>
<template>
  <FileUpload
    mode="basic"
    chooseLabel="选择文件"
    @select="onFileChooseHandler"
    class="h-10 m-8"
  />
  <div class="flex justify-end gap-2">
    <Button
      type="button"
      label="取消"
      severity="secondary"
      @click="closeDialog"
    ></Button>
    <Button type="button" :label="submitText" @click="uploadHandler"></Button>
  </div>
</template>
