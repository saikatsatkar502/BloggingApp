import { privateMyAxios } from "../Api/Api";

class UserService {

    getUserList() {
        return privateMyAxios.get("/users/get-all");
    }

}
export default new UserService();