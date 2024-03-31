package hello.servlet.web.frontcontroller.v4;

import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.v3.ControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberFormControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberListControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberSaveControllerV3;
import hello.servlet.web.frontcontroller.v4.controller.MemberFormControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberListControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberSaveControllerV4;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@WebServlet(name = "frontControllerServletV4", value = "/front-controller/v4/*")
public class FrontControllerServletV4 extends HttpServlet {
    private Map<String, ControllerV4> controllerMap = new HashMap<>();
    public FrontControllerServletV4(){
        controllerMap.put("/front-controller/v4/members/new-form",new MemberFormControllerV4());
        controllerMap.put("/front-controller/v4/members/save",new MemberSaveControllerV4());
        controllerMap.put("/front-controller/v4/members",new MemberListControllerV4());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 요청 url에 맞는 컨트롤러를 찾는 과정
        String requestURI = request.getRequestURI();
        ControllerV4 controller = controllerMap.get(requestURI);
        if(controller == null){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

        // 컨트롤러에 요청 데이터를 건네주는 과정
        // 요청 데이터를 받은 paramMap 을 만든 후 요청 데이터를 넣는다.
        Map<String, String> paramMap = createParamMap(request);
        Map<String, Object> model = new HashMap<>(); // model 은 x001이란 곳에 객체가 생성됨 그 객체에 데이터를 주고 받겠다는 아이디어임

        String viewName = controller.process(paramMap,model); // 컨트롤러는 로직을 수행한 후 mv객체에 viwePath와 로직 처리된 데이터를 model에 담은 mv객체를 반환옴

        //찾은 뷰패스를 통해 뷰가 어딨는지 알려줌
        MyView myView = viewResolver(viewName);
        // 마이뷰 인스턴스가 렌더 과정에서 jsp로 포워드가 일어남
        myView.render(model,request,response);

    }

    private static MyView viewResolver(String viewName) {
        MyView myView = new MyView("/WEB-INF/views/"+ viewName +".jsp");
        return myView;
    }

    private static Map<String, String> createParamMap(HttpServletRequest request) {
        Map<String,String> paramMap = new HashMap<>();
        request.getParameterNames().asIterator()
                .forEachRemaining(paramName-> paramMap.put(paramName, request.getParameter(paramName)));
        return paramMap;
    }
}
