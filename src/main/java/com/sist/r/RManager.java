package com.sist.r;

import org.rosuda.REngine.Rserve.RConnection;
import org.springframework.stereotype.Component;

@Component
public class RManager {
	/* 
	 *  /home/sist/springDev/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/SpringMongoDBProject/ 
	 *  */
	public void boardGraph(int no){
		try {
			RConnection rc=new RConnection();
			rc.voidEval("library(KoNLP)");
			rc.voidEval("library(rJava)");
			rc.voidEval("library(RMongo)");
			rc.voidEval("png(\"/home/sist/springDev/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/SpringMongoDBProject/board/result.png\")");
			rc.voidEval("mongo<-mongoDbConnect(\"mydb\", \"211.238.142.104\", port = 27017)");
			rc.voidEval("data<-dbGetQuery(mongo, \"board\", \"{no:"+no+"}\")");
			rc.voidEval("data2<-sapply(data$content, extractNoun, USE.NAMES = F)");
			rc.voidEval("data3<-unlist(data2)");
			rc.voidEval("data4<-Filter(function(x){nchar(x)>=2}, data3)");
			rc.voidEval("data5<-table(data4)");
			rc.voidEval("data6<-head(sort(data5, decreasing = T), 10)");//많은거 부터 10개
			rc.voidEval("bp<-barplot(data6, col = rainbow(10), cex.names = 0.7, las=2, ylim = c(0, 25))");
			rc.voidEval("pct<-round(data6/sum(data6)*100, 1)");
			rc.voidEval("text(x=bp, y=data6+1, labels = paste(\"(\", pct, \"%\", \")\"), col=\"black\", cex=0.7)");
			rc.voidEval("text(x=bp, y=data6-1, labels = paste(data6, \"건\"), col=\"black\", cex=0.7)");
			rc.voidEval("dev.off()");
			rc.close();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		
	}
	
}




