import axios from "axios";
import axiosInstance from "./AxiosInstance";

interface FileMap {
  id?: number;
  size: number;
  filename: string;
  format: string;
  isDirectory: boolean;
  parent: number;
  owner: number;
  isRoot: boolean;
  filePath: string;
  createTime: string;
  updateTime: string;
}

interface FileMapResponse {
  current: FileMap;
  children: FileMap[];
}

// async function getRootFileList(): Promise<FileMap[]> {
//   try {
//     var resp = await axiosInstance.get(`/filemap/root`);
//     return resp.data;
//   } catch (error) {
//     throw new Error("Error while getting file list");
//   }
// }

async function getFileList(parentId: number): Promise<FileMapResponse> {
  try {
    var resp = await axiosInstance.get<FileMapResponse>(`/filemap/${parentId}`);
    return resp.data;
  } catch (error) {
    throw new Error("Error while getting file list");
  }
}

async function postFolder(name: string, parent: number) {
  try {
    var resp = await axiosInstance.post(`/filemap/dir`, {
      dirName: name,
      parentId: parent,
    });
    return resp.data;
  } catch (error) {
    throw new Error("Error while creating folder");
  }
}

async function postNewFile(newFile: any, parentId: number): Promise<string> {
  newFile.processing = "读取中……";
  const sha256 = await calculateFileSHA256(newFile.file);

  try {
    await axiosInstance.get(
      `filemap/file?sha256=${sha256}&parentId=${parentId}&filename=${newFile.file_name}&format=${newFile.format}`
    );
    newFile.processing = "完成";
    return "linked";
  } catch (error) {
    // 判断错误码是不是404，如果是404，说明文件不存在，可以上传
    if (axios.isAxiosError(error) && error.response?.status === 400) {
      const formData = new FormData();
      formData.append("filename", newFile.file_name);
      formData.append("format", newFile.format);
      formData.append("file", newFile.file);
      formData.append("parentId", parentId.toString());
      formData.append("sha256", sha256);
      const response = await axiosInstance.post("filemap/file", formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
        // 回调，用于更新上传进度
        onUploadProgress: function (progressEvent) {
          let percentCompleted = Math.round(
            (progressEvent.loaded * 100) / progressEvent.total!
          );
          newFile.processing = percentCompleted;
        },
      });
      return "success";
    }
    return "error";
  }
}

async function deleteFile(fileId: number): Promise<boolean> {
  try {
    const response = await axiosInstance.delete(`filemap/file/${fileId}`);
    return response.status.toString().startsWith("2");
  } catch (error) {
    console.error(`Error occurred while deleting file record: ${error}`);
    throw error;
  }
}

async function patchFile(fileId: number, newName: string): Promise<boolean> {
  try {
    const response = await axiosInstance.patch(`filemap/file/${fileId}`, {
      filename: newName,
    });
    return response.status.toString().startsWith("2");
  } catch (error) {
    console.error(`Error occurred while patching file record: ${error}`);
    throw error;
  }
}

async function calculateFileSHA256(file: File): Promise<string> {
  const arrayBuffer = await file.arrayBuffer();
  const hashBuffer = await crypto.subtle.digest("SHA-256", arrayBuffer);
  const hashArray = Array.from(new Uint8Array(hashBuffer));
  const hashHex = hashArray
    .map((b) => b.toString(16).padStart(2, "0"))
    .join("");
  return hashHex;
}

export {
  getFileList,
  postFolder,
  postNewFile,
  deleteFile,
  patchFile,
  calculateFileSHA256,
};
export type { FileMap };
