function follow(check, userId){
	// true -> follow 하기
	// false -> unFollow 하기
	let url = "/follow/"+userId;
	if(check){
		fetch(url,{
			method: "POST"
	    }).then(function(res){
			console.log("res1",res);
			return res.text();							
		}).then(function(res){
			console.log("res2",res);

			if(res === "ok"){
				let follow_check_el = document.querySelector("#follow_check");
				console.log("follow_check",follow_check_el);
				console.log("follow_check.inner",follow_check_el.innerHTML);
				follow_check_el.innerHTML = "<button onClick='follow(false, "+userId+")' class='profile_edit_btn'>팔로잉</button>";
			}
		});
	}else{
		fetch(url,{
			method: "DELETE"
	    }).then(function(res){
			console.log("res3",res);
			return res.text();							
		}).then(function(res){
			console.log("res4",res);

			if(res === "ok"){
				let follow_check_el = document.querySelector("#follow_check");
				console.log("follow_check",follow_check_el);
				follow_check_el.innerHTML = "<button onClick='follow(true, "+userId+")' class='profile_follow_btn'>팔로우</button>";
			}
		});
	}
}