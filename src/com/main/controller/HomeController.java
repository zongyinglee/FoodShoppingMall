package com.main.controller;

import com.main.bean.*;
import com.main.utils.Base64Util;
import com.main.utils.MailUtils;
import com.main.utils.SqlHelper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;

import static com.main.utils.ConstUtil.*;


@Controller
@RequestMapping("/home")
public class HomeController {

    @RequestMapping("/view")
    public String banner() {
        return "/view/home";
    }


    @RequestMapping(value="/action",method=RequestMethod.POST)
    public void action(HttpServletRequest req, HttpServletResponse res) throws IOException, MessagingException {
        //调整编码，防止中文乱码
        req.setCharacterEncoding("utf-8");
        res.setCharacterEncoding("utf-8");
        //获取动作
        String action = req.getParameter("action");
        //获取回写对象
        PrintWriter printWriter = res.getWriter();
        //动作分发
        switch (action) {
            case ACTION_HOME_GET_HOT_GOODS:
                printWriter.print(doGetHotGoods(req));
                break;
            case ACTION_HOME_LOGIN:
                printWriter.print(doLogin(req));
                break;
            case ACTION_HOME_SIGN_UP:
                printWriter.print(doSignUp(req));
                break;
            case ACTION_HOME_GET_GOODS_DETAIL:
                printWriter.print(doGetGoodsDetail(req));
                break;
            case ACTION_HOME_GET_CAR:
                printWriter.print(doGetCar(req));
                break;
            case ACTION_HOME_ADD_CAR:
                printWriter.print(doAddCar(req));
                break;
            case ACTION_HOME_GET_ADDR:
                printWriter.print(dogGetAddr(req));
                break;
            case ACTION_HOME_ADD_ADDR:
                printWriter.print(dogAddAddr(req));
                break;
            case ACTION_HOME_EDIT_ADDR:
                printWriter.print(dogEditAddr(req));
                break;
            case ACTION_HOME_DEL_ADDR:
                printWriter.print(dogDelAddr(req));
                break;
            case ACTION_HOME_ADD_ORDER:
                printWriter.print(dogAddOrder(req));
                break;
            case ACTION_HOME_DEL_CAR:
                printWriter.print(doDelCar(req));
                break;
            case ACTION_HOME_SEARCH:
                printWriter.print(doSearch(req));
                break;
            case ACTION_HOME_GET_ORDER:
                printWriter.print(doGetOrder(req));
                break;
            case ACTION_HOME_DEL_ORDER:
                printWriter.print(doDelOrder(req));
                break;
        }
    }


    private String doGetHotGoods(HttpServletRequest req) {
        String sql = "select * from goods order by id desc limit 9";
        List<GoodsBean> goodsBeans = SqlHelper.doListQuery(sql,null,GoodsBean.class);
        if (null != goodsBeans) {
            JSONArray jsonArray = JSONArray.fromObject(goodsBeans);
            return jsonArray.toString();
        } else {
            return "";
        }
    }


    private String doLogin(HttpServletRequest req) {
        String sql = "select * from user where phone=? and pwd=? and state = 1";
        String[] p = {
                req.getParameter("phone"),
                Base64Util.encode(req.getParameter("pwd"))
        };
        UserBean userBean = SqlHelper.doObjQuery(sql,p,UserBean.class);
        if (userBean != null) {
            //登录成功
            JSONObject jsonObject = JSONObject.fromObject(userBean);
            return jsonObject.toString();
        } else {
            //登录失败
            return "";
        }
    }


    private Boolean doSignUp(HttpServletRequest req) throws MessagingException {
        String sql = "insert into user(phone,name,pwd,state,code) select ?,?,?,?,? from dual where not exists(select name from user where phone=?)";
        String activeCode = UUID.randomUUID().toString().replace("-","");
        String[] p = {
                //req.getParameter("name"),
                req.getParameter("phone"),
                req.getParameter("name"),
                Base64Util.encode(req.getParameter("pwd")),
                "0",
                activeCode,
                req.getParameter("phone")
        };
        int result = SqlHelper.doUpdate(sql,p);
        if (result > 0) {
            String emailMsg = "欢迎光临我们的零食商城w恭喜您注册成功，请点击下面的连接进行激活账户"
                    + "<a href='http://localhost:8080/active?activeCode="+activeCode+"'>"
                    + "http://localhost:8080/active?activeCode="+activeCode+"</a>";
            //发送激活邮件
            MailUtils.sendMail(req.getParameter("name"), emailMsg);
            return true;
        } else {
            return false;
        }
    }


    private String doGetGoodsDetail(HttpServletRequest req) {
        String sql = "select * from goods where id=?";
        String[] p = {
                req.getParameter("id")
        };
        GoodsBean goodsBean = SqlHelper.doObjQuery(sql,p,GoodsBean.class);
        if (goodsBean != null) {
            JSONObject jsonObject = JSONObject.fromObject(goodsBean);
            return jsonObject.toString();
        } else {
            return "";
        }
    }


    private String doGetCar(HttpServletRequest req) {
        String sql = "select car.*,goods.id goodsid,goods.title,goods.detail,goods.img,goods.price from car,goods where car.userid=? and car.goodid=goods.id order by car.id desc";
        String[] p = {
                req.getParameter("id")
        };
        List<CarBean> carBeans = SqlHelper.doListQuery(sql,p,CarBean.class);
        if (null != carBeans) {
            JSONArray jsonArray = JSONArray.fromObject(carBeans);
            return jsonArray.toString();
        } else {
            return "";
        }
    }


    private Boolean doAddCar(HttpServletRequest req) {
        String sql = "insert into car(goodid,userid,count) values(?,?,?)";
        String[] p = {
                req.getParameter("goodid"),
                req.getParameter("userid"),
                req.getParameter("count")
        };
        int result = SqlHelper.doUpdate(sql,p);
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }


    private String dogGetAddr(HttpServletRequest req) {
        String sql = "select * from addr where userid=? order by id desc";
        String[] p = {
                req.getParameter("userid")
        };
        List<AddrBean> addrBeans = SqlHelper.doListQuery(sql,p,AddrBean.class);
        if (null != addrBeans) {
            JSONArray jsonArray = JSONArray.fromObject(addrBeans);
            return jsonArray.toString();
        } else {
            return "";
        }
    }


    private Boolean dogAddAddr(HttpServletRequest req) {
        String sql = "insert into addr(name,phone,addr,userid) values(?,?,?,?)";
        String[] p = {
                req.getParameter("name"),
                req.getParameter("phone"),
                req.getParameter("addr"),
                req.getParameter("userid")
        };
        int result = SqlHelper.doUpdate(sql,p);
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }


    private Boolean dogEditAddr(HttpServletRequest req) {
        String sql = "update addr set name=?,phone=?,addr=? where id=?";
        String[] p = {
                req.getParameter("name"),
                req.getParameter("phone"),
                req.getParameter("addr"),
                req.getParameter("id")
        };
        int result = SqlHelper.doUpdate(sql,p);
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }


    private Boolean dogDelAddr(HttpServletRequest req) {
        String sql = "delete from addr where id=?";
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


    private Boolean dogAddOrder(HttpServletRequest req) {
        String car = req.getParameter("car_list");
        String[] car_list = car.split(",");
        for (int i=0;i<car_list.length;i++) {
            String sql = "delete from car where id=?";
            String[] p = {car_list[i]};
            int result = SqlHelper.doUpdate(sql,p);
            if (result>0) {
                System.out.println("成功!");
            } else {
                System.out.println("失败!");
            }
        }
        String sql = "insert into orders(userid,addrid,list,time,total) values(?,?,?,?,?)";
        String[] p = {
                req.getParameter("userid"),
                req.getParameter("addrid"),
                req.getParameter("list"),
                req.getParameter("time"),
                req.getParameter("total")
        };
        int result = SqlHelper.doUpdate(sql,p);
        if (result>0) {
            return true;
        } else {
            return false;
        }
    }


    private Boolean doDelCar(HttpServletRequest req) {
        String sql = "delete from car where id=?";
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


    private String doSearch(HttpServletRequest req) {
        String sql = "select * from goods where title like ? or ? like CONCAT('%',title,'%') order by id desc";
        String[] p = {
                "%" + req.getParameter("key") + "%",
                req.getParameter("key")
        };
        List<GoodsBean> goodsBeans = SqlHelper.doListQuery(sql,p,GoodsBean.class);
        if (null != goodsBeans) {
            JSONArray jsonArray = JSONArray.fromObject(goodsBeans);
            return jsonArray.toString();
        } else {
            return "";
        }
    }


    private String doGetOrder(HttpServletRequest req) {
        String sql = "select orders.*,addr.name,addr.phone,addr.addr from orders,addr where orders.userid=? and orders.addrid=addr.id order by orders.id desc";
        String[] p = {
                req.getParameter("userid")
        };
        List<OrdersBean> ordersBeans = SqlHelper.doListQuery(sql,p,OrdersBean.class);
        if (null != ordersBeans) {
            JSONArray jsonArray = JSONArray.fromObject(ordersBeans);
            return jsonArray.toString();
        } else {
            return "";
        }
    }


    private Boolean doDelOrder(HttpServletRequest req) {
        String sql = "update orders set state=1 where id=?";
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
}
