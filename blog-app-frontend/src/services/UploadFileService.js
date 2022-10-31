import { privateMyAxios } from "../Api/Api";

export default class UploadFileService {

    ulpoadImage(file, autherEmail, postTitle) {

        let formData = new FormData();
        formData.append("file", file);
        return privateMyAxios.post(`/upload-image?`, formData, {
            headers: {
                "Content-Type": "multipart/form-data"
            }


        });
    }


}