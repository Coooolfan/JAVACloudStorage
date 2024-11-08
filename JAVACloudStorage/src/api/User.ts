import axiosInstance from "./AxiosInstance";

interface User {
  username: string;
  avatar: string;
}

const defaultUser = {
  username: "user",
  avatar: "https://zhifengmuxue.top/img/1.jpg",
};

async function getUser(): Promise<User> {
  const resp = await axiosInstance.get<User>("/user/info");
  return resp.data;
}

export { defaultUser, getUser };
export type { User };
