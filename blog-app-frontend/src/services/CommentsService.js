import { myAxios, privateMyAxios } from "../Api/Api";

class CommentsService {

    async addComment(comment, postId, autherEmail) {
        const response = await privateMyAxios.post(`/comments/create?userEmail=${autherEmail}&postId=${postId}`, comment);
        return response.data;
    }

    async getAllCommentsByPostId(postId) {
        const response = await myAxios.get(`/comments/get-by-post-id?postId=${postId}`);
        return response.data;
    }

}

//comments/create?userEmail=user2@user.com&postId=4

export default new CommentsService();