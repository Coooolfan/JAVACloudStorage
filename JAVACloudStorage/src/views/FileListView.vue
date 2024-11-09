<script setup lang="ts">
import {
  deleteFile,
  getFileList,
  postFolder,
  type FileMap,
} from "@/api/FileMap";
import Button from "primevue/button";
import { onMounted, ref } from "vue";
import DataTable from "primevue/datatable";
import Column from "primevue/column";
import NewFloderDialog from "@/components/NewFloderDialog.vue";
import NewFileDialog from "@/components/NewFileDialog.vue";
import { useDialog } from "primevue/usedialog";
import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";
import ConfirmPopup from "primevue/confirmpopup";
import PatchFileMapDialog from "@/components/PatchFileMapDialog.vue";
import { useRoute } from "vue-router";
import router from "@/router";
import { data } from "autoprefixer";

const route = useRoute();
const toast = useToast();
const dialog = useDialog();
const confirm = useConfirm();

const lastDirId = ref(-1);
const dirId = ref(route.params.id);
const fileList = ref<Array<FileMap>>([]);
const current = ref<FileMap>({
  id: -1,
  filename: "",
  format: "",
  size: 0,
  isDirectory: true,
  createTime: "",
  updateTime: "",
  parent: 0,
  owner: 0,
  isRoot: false,
  filePath: "",
});
// 监听路由变化
router.afterEach((to, from) => {
  lastDirId.value = Number(from.params.id);
  refreshFileList();
});

onMounted(() => {
  refreshFileList();
});

async function refreshFileList() {
  dirId.value = route.params.id; // 更新当前目录
  console.log("dirId", dirId.value);
  if (dirId.value === undefined) {
    window.location.href = "/dashboard/-1";
  }
  const resp = await getFileList(Number(dirId.value));
  fileList.value = resp.children;
  current.value = resp.current;
  fileList.value.forEach((file) => {
    file.createTime = new Date(
      new Date(file.createTime).getTime() + 8 * 60 * 60 * 1000
    ).toLocaleString();
    file.updateTime = new Date(
      new Date(file.updateTime).getTime() + 8 * 60 * 60 * 1000
    ).toLocaleString();
  });
  fileList.value.sort((a, b) => {
    // 先按 isDirectory 排序
    if (a.isDirectory !== b.isDirectory) {
      return a.isDirectory ? -1 : 1;
    }
    // 再按 updateTime 排序
    return new Date(b.updateTime).getTime() - new Date(a.updateTime).getTime();
  });
  if (Number(dirId.value) === -1 || current.value.isRoot) {
    return;
  }
  // 加到第一个
  fileList.value.unshift({
    id: current.value.parent,
    filename: "..",
    format: "",
    size: 0,
    isDirectory: true,
    createTime: "",
    updateTime: "",
    parent: 0,
    owner: 0,
    isRoot: false,
    filePath: "",
  });
}

function displayNewFloderDialog() {
  dialog.open(NewFloderDialog, {
    props: {
      modal: true,
      closable: false,
      header: "新建文件夹",
    },
    data: {
      dirId: Number(dirId.value),
    },
  });
}

function displayNewFileDialog() {
  dialog.open(NewFileDialog, {
    props: {
      modal: true,
      closable: false,
      header: "上传文件",
    },
    data: {
      dirId: Number(dirId.value),
    },
  });
}

const beautySize = (data: FileMap) => {
  if (data.isDirectory) {
    return "-";
  }
  const size = data.size;
  if (size < 1024) {
    return size + " KB";
  } else if (size < 1024 * 1024) {
    return (size / 1024).toFixed(2) + " MB";
  } else {
    return (size / 1024 / 1024).toFixed(2) + " GB";
  }
};

async function downloadHandler(data: FileMap) {
  const a = document.createElement("a");
  a.href = `/api/filemap/file/${data.id}`;
  a.download =
    data.format.length > 0 ? `${data.filename}.${data.format}` : data.filename;
  a.click();
}

async function deleteHandler(id: number) {
  const resp = await deleteFile(id);
  if (resp) {
    toast.add({
      severity: "success",
      summary: "删除成功",
      detail: `文件删除成功`,
      life: 3000,
    });
    fileList.value = fileList.value.filter((file) => file.id !== id);
  } else {
    toast.add({
      severity: "error",
      summary: "删除失败",
      detail: `文件删除失败`,
      life: 3000,
    });
  }
}

function confirmDel(event: { currentTarget: any }, id: number) {
  confirm.require({
    target: event.currentTarget,
    message: "确认删除此文件？",
    icon: "pi pi-info-circle",
    rejectProps: {
      label: "手滑了~",
      severity: "secondary",
      outlined: true,
    },
    acceptProps: {
      label: "删除",
      severity: "danger",
    },
    accept: () => {
      deleteHandler(id);
    },
  });
}

function displayFilenameEditor(data: FileMap) {
  dialog.open(PatchFileMapDialog, {
    props: {
      modal: true,
      closable: false,
      header: "编辑文件名",
    },
    data: {
      id: data.id,
      filename: data.filename,
    },
  });
}

function routerPusher(data: FileMap) {
  if (data.isDirectory) {
    router.push(`/dashboard/${data.id}`);
  }
}
</script>
<template>
  <ConfirmPopup />

  <div class="w-full ml-8 mr-8">
    <div class="flex justify-end gap-8 mt-4">
      <Button @click="displayNewFileDialog">上传</Button>
      <Button severity="secondary" @click="displayNewFloderDialog"
        >新建文件夹</Button
      >
    </div>
    <DataTable :value="fileList" stripedRows lazy class="transition-all p-5">
      <Column field="isDirectory" header="">
        <template #body="{ data }">
          <i
            v-if="data.isDirectory"
            class="pi pi-folder"
            :class="{ 'cursor-pointer': data.isDirectory }"
            style="font-size: 1.5rem"
            @click="routerPusher(data)"
          ></i>
          <i v-else class="pi pi-file" style="font-size: 1.5rem"></i>
        </template>
      </Column>
      <Column field="filename" header="文件名">
        <template #body="{ data }">
          <div class="flex justify-between">
            <span> {{ data.filename }}</span>
            <div class="gap-3 flex">
              <i
                v-if="!data.isDirectory"
                class="pi pi-download cursor-pointer"
                @click="downloadHandler(data)"
              ></i>
              <i
                class="pi pi-file-edit cursor-pointer"
                @click="displayFilenameEditor(data)"
              ></i>
              <i
                class="pi pi-trash cursor-pointer"
                @click="confirmDel($event, data.id)"
              ></i>
            </div>
          </div>
        </template>
      </Column>
      <Column field="format" header="文件类型"></Column>
      <Column field="size" header="文件大小">
        <template #body="{ data }">{{ beautySize(data) }}</template>
      </Column>
      <Column field="createTime" header="创建时间">
        <template #body="{ data }"
          ><span> {{ data.createTime }} </span></template
        >
      </Column>
      <Column field="updateTime" header="修改时间"></Column
    ></DataTable>
  </div>
</template>
