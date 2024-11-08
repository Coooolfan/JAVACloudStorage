import axios from "axios";

const axiosInstance = axios.create({
  baseURL: "/api",
  headers: {
    "Content-Type": "application/json",
  },
});

// 响应拦截器
axiosInstance.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    /**
     * 401: 未登录
     */
    if (
      error.response.status.toString() === "401" &&
      !(error.config.url === "token/")
    ) {
      alert("登录无效/过期，请重新登录");
      location.href = window.location.origin + "/login";
    } else {
      return Promise.reject(error);
    }
  }
);

export default axiosInstance;
