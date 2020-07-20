package com.main.controller;

import com.main.bean.GoodsBean;
import com.main.bean.OrdersBean;
import com.main.bean.UserBean;
import com.main.utils.Base64Util;
import com.main.utils.SqlHelper;
import net.sf.json.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static com.main.utils.ConstUtil.*;


@Controller
@RequestMapping("/admin")
public class AdminController {

    @RequestMapping("/view")
    public String admin() {
        return "/view/admin";
    }

    @RequestMapping(value="/action",method= RequestMethod.POST)
    public void action(HttpServletRequest req, HttpServletResponse res) throws IOException {
        //调整编码，防止中文乱码
        req.setCharacterEncoding("utf-8");
        res.setCharacterEncoding("utf-8");
        //获取动作
        String action = req.getParameter("action");
        //获取回写对象
        PrintWriter printWriter = res.getWriter();
        //动作分发
        switch (action) {
            case ACTION_ADMIN_GET_GOODS:
                printWriter.print(doAdminGetGoods(req));
                break;
            case ACTION_ADMIN_ADD_GOODS:
                printWriter.print(doAdminAddGoods(req));
                break;
            case ACTION_ADMIN_EDIT_GOODS:
                printWriter.print(doAdminEditGoods(req));
                break;
            case ACTION_ADMIN_DEL_GOODS:
                printWriter.print(doAdminDelGoods(req));
                break;
            case ACTION_ADMIN_GET_ORDER:
                printWriter.print(doGetOrder(req));
                break;
            case ACTION_ADMIN_DEL_ORDER:
                printWriter.print(doDelOrder(req));
                break;
            case ACTION_ADMIN_GET_USER:
                printWriter.print(doGetUser(req));
                break;
            case ACTION_ADMIN_EDIT_USER:
                printWriter.print(doEditUser(req));
                break;
        }
    }

    private String doAdminGetGoods(HttpServletRequest req) {
        String sql = "select * from goods order by id desc";
        List<GoodsBean> goodsBeans = SqlHelper.doListQuery(sql,null,GoodsBean.class);
        if (null != goodsBeans) {
            JSONArray jsonArray = JSONArray.fromObject(goodsBeans);
            return jsonArray.toString();
        } else {
            return "";
        }
    }


    private Boolean doAdminAddGoods(HttpServletRequest req) {
        String sql1 = "select * from goods where title = ?";
        String[] p1 = {
                req.getParameter("title")
        };
        GoodsBean result1 = SqlHelper.doObjQuery(sql1,p1,GoodsBean.class);
        if(result1 == null){
            String sql = "insert into goods(title,detail,img,price) values(?,?,?,?)";
            String[] p = {
                    req.getParameter("title"),
                    req.getParameter("detail"),
                    req.getParameter("img"),
                    req.getParameter("price")
            };
            int result2 = SqlHelper.doUpdate(sql,p);
            if (result2 > 0) {
                return true;
            } else {
                return false;
            }
        }
        else{
            return false;
        }
    }


    private Boolean doAdminEditGoods(HttpServletRequest req) {
        String img = req.getParameter("img");
        String sql1 = "select * from goods where title = ?";
        String[] p1 = {
                req.getParameter("title")
        };
        GoodsBean result1 = SqlHelper.doObjQuery(sql1,p1,GoodsBean.class);
        if(result1 == null) {
            if (null == img || img.equals("")) {
                //无图片更新
                String sql = "update goods set title=?,detail=?,price=? where id=?";
                String[] p = {
                        req.getParameter("title"),
                        req.getParameter("detail"),
                        req.getParameter("price"),
                        req.getParameter("id")
                };
                int result = SqlHelper.doUpdate(sql, p);
                if (result > 0) {
                    return true;
                } else {
                    return false;
                }
            } else {
                //更新图片
                String sql = "update goods set title=?,detail=?,img=?,price=? where id=?";
                String[] p = {
                        req.getParameter("title"),
                        req.getParameter("detail"),
                        req.getParameter("img"),
                        req.getParameter("price"),
                        req.getParameter("id")
                };
                int result = SqlHelper.doUpdate(sql, p);
                if (result > 0) {
                    return true;
                } else {
                    return false;
                }
            }
        }else {
            return false;
        }
    }


    private Boolean doAdminDelGoods(HttpServletRequest req) {
        String sql = "delete from goods where id=?";
        String[] p = {
                req.getParameter("id")
        };
        int result = SqlHelper.doUpdate(sql,p);
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }


    private String doGetOrder(HttpServletRequest req) {
        String sql = "select orders.*,addr.name,addr.phone,addr.addr from orders,addr where orders.addrid=addr.id order by orders.id desc";
        List<OrdersBean> ordersBeans = SqlHelper.doListQuery(sql,null,OrdersBean.class);
        if (null != ordersBeans) {
            JSONArray jsonArray = JSONArray.fromObject(ordersBeans);
            return jsonArray.toString();
        } else {
            return "";
        }
    }


    private Boolean doDelOrder(HttpServletRequest req) {
        String sql = "update orders set state=2 where id=?";
        String[] p = {
                req.getParameter("id")
        };
        int result = SqlHelper.doUpdate(sql,p);
        if (result>0) {
            return true;
        } else {
            return false;
        }
    }


    private String doGetUser(HttpServletRequest req) {
        String sql = "select * from user order by id desc";
        List<UserBean> userBeans = SqlHelper.doListQuery(sql,null,UserBean.class);
        if (null != userBeans) {
            JSONArray jsonArray = JSONArray.fromObject(userBeans);
            return jsonArray.toString();
        } else {
            return "";
        }
    }


    private Boolean doEditUser(HttpServletRequest req) {
        String sql = "update user set name=?,pwd=? where id=?";
        String[] p = {
                req.getParameter("name"),
                Base64Util.encode(req.getParameter("pwd")),
                req.getParameter("id")
        };
        int result = SqlHelper.doUpdate(sql,p);
        if (result>0) {
            return true;
        } else {
            return false;
        }
    }
}
