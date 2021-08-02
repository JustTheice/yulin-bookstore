import {ElButton, ElHeader, ElContainer, ElAside, ElMain} from 'element-plus';


export default (app) => {
  app.component(ElButton.name, ElButton);
  app.component(ElHeader.name, ElHeader);
  app.component(ElContainer.name, ElContainer);
  app.component(ElAside.name, ElAside);
  app.component(ElMain.name, ElMain);
}