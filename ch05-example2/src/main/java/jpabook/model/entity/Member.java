package jpabook.model.entity;

import jakarta.persistence.*;

@Entity
public class Member {

    static int tempId = 0;

    @Id
    @Column(name = "MEMBER_ID")
    private String id;

    private String username;

    public Member() {
        this.id = String.valueOf(tempId);
        tempId ++;
    }

    public Member(String id, String username) {
        this.id = id;
        this.username = username;
    }

    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    public void setTeam(Team team) {
        // 기존 팀과의 연관관계 제거
        if (this.team != null) {
            this.team.getMembers().remove(this); // 기존 팀의 members 리스트에서 현재 멤버 제거
        }

        this.team = team; // 새로운 팀 설정

        // 새로운 팀에 현재 멤버 추가 (무한 루프 방지)
        if (team != null && !team.getMembers().contains(this)) {
            team.getMembers().add(this); // 새로운 팀의 members 리스트에 현재 멤버 추가
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Team getTeam() {
        return team;
    }
}
