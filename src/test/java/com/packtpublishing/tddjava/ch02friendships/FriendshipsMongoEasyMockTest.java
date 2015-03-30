package com.packtpublishing.tddjava.ch02friendships;

import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.MockType;
import org.easymock.TestSubject;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.*;

@RunWith(EasyMockRunner.class)
public class FriendshipsMongoEasyMockTest {
    @TestSubject
    FriendshipsMongo friendships = new FriendshipsMongo();

    @Mock(type = MockType.NICE)
    FriendsCollection friends;

    @Test
    public void test1() {
        expect(friends.findByName(anyString())).andReturn(new Person("Joe"));

        replay(friends);

        assertThat(friendships.getFriendsList("Joe")).isEmpty();

        verify(friends);
    }

    @Test
    public void test2() {
        Person joe = new Person("Joe");
        expect(friends.findByName("Joe")).andReturn(joe).anyTimes();

        Person audrey = new Person("Audrey");
        expect(friends.findByName("Audrey")).andReturn(audrey).anyTimes();

        replay(friends);

        friendships.makeFriends("Joe", "Audrey");

        verify(friends);

        assertThat(friendships.areFriends("Joe", "Audrey")).isTrue();
        assertThat(friendships.areFriends("Audrey", "Joe")).isTrue();

        assertThat(friendships.getFriendsList("Joe")).hasSize(1).contains("Audrey");

        assertThat(friendships.getFriendsList("Audrey")).hasSize(1).contains("Joe");
    }

    @Test(timeout = 500)
    public void test3() {
        Person joe = new Person("Joe");
        expect(friends.findByName("Joe")).andReturn(joe).anyTimes();

        //doNothing().when(friends).save(any(Person.class));
        replay(friends);

        friendships.makeFriends("Joe", "Audrey");
        friendships.makeFriends("Joe", "Peter");
        friendships.makeFriends("Joe", "Michael");
        friendships.makeFriends("Joe", "Britney");
        friendships.makeFriends("Joe", "Paul");

        verify(friends);

        assertThat(friendships.areFriends("Joe", "Paul")).isTrue();
        assertThat(friendships.areFriends("Audrey", "Ralph")).isFalse();

        assertThat(friendships.getFriendsList("Joe")).containsOnly("Audrey", "Peter", "Michael", "Britney", "Paul");
    }
}
