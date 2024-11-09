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
  try {
    const formData = new FormData();
    formData.append("filename", newFile.file_name);
    formData.append("format", newFile.format);
    formData.append("file", newFile.file);
    formData.append("parentId", parentId.toString());
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
  } catch (error) {
    if (axios.isAxiosError(error) && error.response) {
      return error.response.data;
    }
    return "Unexpected Error";
    console.error(`Error occurred while posting file record: ${error}`);
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

export { getFileList, postFolder, postNewFile, deleteFile, patchFile };
export type { FileMap };
