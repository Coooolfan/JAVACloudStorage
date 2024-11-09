import axiosInstance from "./AxiosInstance";

interface User {
  username: string;
  avatar: string;
  usedQuota: number;
  quota: number;
}

const defaultUser = {
  username: "user",
  avatar: "https://zhifengmuxue.top/img/1.jpg",
  usedQuota: 0,
  quota: 100,
};

async function getUser(): Promise<User> {
  const resp = await axiosInstance.get<User>("/user/info");
  return resp.data;
}

export { defaultUser, getUser };
export type { User };
