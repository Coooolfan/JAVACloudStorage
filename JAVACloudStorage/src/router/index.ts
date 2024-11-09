import { createRouter, createWebHistory } from "vue-router";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/dashboard/:id", // 动态路由参数 :id
      name: "dashboard",
      component: () => import("@/views/FileListView.vue"),
    },
  ],
});

export default router;
