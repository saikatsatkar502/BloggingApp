package com.blogapp.backend.service.role;

import com.blogapp.backend.exception.MethodArgumentsNotFound;
import com.blogapp.backend.model.Role;
import com.blogapp.backend.repo.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleInterface {

    @Autowired private RoleRepository roleRepo;

    public List<Role> findAllRoles() {
        return roleRepo.findAll();
    }

    @Override
    public String findRoleNameById(int id) {
        if(id==0) {
            throw new MethodArgumentsNotFound("Id", "findRoleNameById", id);
        }
       return this.roleRepo.findNameById(id);
    }

    @Override
    public int findRoleIdByName(String name) {
        if(name==null) {
            throw new MethodArgumentsNotFound("Name field id null at findRoleIdByName");
        }
        return this.roleRepo.findByName(name).getId();
    }

    @Override
    public Role findRoleByName(String name) {
        if(name==null) {
            throw new MethodArgumentsNotFound("Name filed is null at findRoleByName");
        }
        return this.roleRepo.findByName(name);
    }

}
