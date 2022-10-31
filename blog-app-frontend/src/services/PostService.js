import { myAxios, privateMyAxios } from "../Api/Api";

class PostService {

    async addpost(post, autherEmail) {
        const response = await privateMyAxios.post(`/post/create/${autherEmail}`, post);
        return response.data;
    }

    async getAllPosts(paageNo, pageSize) {
        const response = await myAxios.get("/post/get-by-page?pageNo=" + paageNo + "&pageSize=" + pageSize + "&sortBy=createdAt&sortDirection=DESC");
        return response.data;
    }

}

export default new PostService();