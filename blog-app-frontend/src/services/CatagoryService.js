import { myAxios } from "../Api/Api";

class CatagoryService {

    getAllCatagory() {
        return myAxios.get("/category/get-all");
    }
}

export default new CatagoryService();