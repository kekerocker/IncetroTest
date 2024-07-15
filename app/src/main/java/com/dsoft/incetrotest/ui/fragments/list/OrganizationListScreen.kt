package com.dsoft.incetrotest.ui.fragments.list

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.dsoft.domain.model.Organization
import com.dsoft.incetrotest.R
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.dsoft.domain.util.Response
import com.dsoft.incetrotest.ui.theme.backgroundMain
import com.dsoft.incetrotest.ui.theme.colorAccentBlue
import com.dsoft.incetrotest.ui.utils.ComposableLifecycle
import com.dsoft.incetrotest.ui.utils.LoadingScreen

@Composable
fun OrganizationListScreen(
    navController: NavController,
    viewModel: OrganizationListViewModel = hiltViewModel()
) {
    ComposableLifecycle { _, event ->
        if (event == Lifecycle.Event.ON_RESUME) {
            viewModel.fetchOrganizationsList()
        }
    }

    ConstraintLayout(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .fillMaxSize()
            .background(color = backgroundMain)
    ) {
        val (toolbar, listView) = createRefs()
        val organizationListState = viewModel.organizationList.collectAsState()

        when (organizationListState.value) {
            is Response.Success -> {
                MyToolbar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .background(backgroundMain)
                        .constrainAs(toolbar, constrainBlock = {
                            top.linkTo(parent.top)
                        }),
                    heartCount = organizationListState.value.data?.count { it.isFavorite } ?: 0,
                    viewmodel = viewModel,
                )
            }
            is Response.Failure -> {
                Toast.makeText(LocalContext.current, organizationListState.value.exception?.message, Toast.LENGTH_SHORT).show()
            }
            is Response.Loading -> LoadingScreen()
        }

        OrganizationList(
            navController = navController,
            modifier = Modifier
                .constrainAs(
                    ref = listView,
                    constrainBlock = {
                        top.linkTo(toolbar.bottom)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.fillToConstraints
                    })
                .fillMaxWidth(),
            viewModel
        )

    }
}

@Composable
private fun OrganizationList(
    navController: NavController,
    modifier: Modifier,
    viewmodel: OrganizationListViewModel
) {
    val organizationList = remember { viewmodel.organizationList }
    val isFavoriteState = remember { viewmodel.filterFavorite }
    val list = organizationList.collectAsState().value
    val isFavorite = isFavoriteState.collectAsState().value

    when (list) {
        is Response.Success -> {
            LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(list.data.let {
                    if (isFavorite) it?.filter { item -> item.isFavorite }.orEmpty() else it.orEmpty()
                }) { organization ->
                    OrganizationEntry(
                        entry = organization,
                        navController = navController,
                        viewmodel = viewmodel
                    )
                }
            }
        }
        is Response.Failure -> {
            Toast.makeText(LocalContext.current, list.exception.message, Toast.LENGTH_SHORT).show()
        }
        is Response.Loading -> LoadingScreen()
    }

}

@Composable
private fun MyToolbar(
    modifier: Modifier,
    heartCount: Int,
    viewmodel: OrganizationListViewModel
) {
    ConstraintLayout(
        modifier = modifier
    ) {
        val (text, heartIcon) = createRefs()

        Text(
            text = stringResource(id = R.string.restaurants),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .constrainAs(ref = text, constrainBlock = {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })
                .padding(horizontal = 16.dp)
        )

        HeartIconWithCount(
            heartCount = heartCount,
            modifier = Modifier
                .padding(end = 16.dp)
                .constrainAs(heartIcon, constrainBlock = {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                })
                .clickable {
                    viewmodel.switchFavoriteState()
                },
            viewmodel = viewmodel
        )
    }
}

@Composable
private fun HeartIconWithCount(
    heartCount: Int,
    modifier: Modifier,
    viewmodel: OrganizationListViewModel
) {
    val isFavoriteState = viewmodel.filterFavorite.collectAsState()
    val icon = if (isFavoriteState.value) R.drawable.ic_favorite else R.drawable.ic_not_favorite
    val textColor = if (isFavoriteState.value) Color.White else colorAccentBlue

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = colorAccentBlue,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = heartCount.toString(),
            color = textColor,
            fontSize = 10.sp
        )
    }
}

@Composable
private fun OrganizationEntry(
    entry: Organization,
    navController: NavController,
    viewmodel: OrganizationListViewModel
) {
    val favoriteImage =
        if (entry.isFavorite) R.drawable.ic_favorite else R.drawable.ic_not_favorite

    ConstraintLayout(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(24.dp, 24.dp, 24.dp, 24.dp))
            .background(Color.White)
            .clickable {
                navController.navigate(route = "organization_details_screen/${entry.id}")
            }) {
        val (organizationImageView,
            titleView,
            starImageView,
            cuisinesView,
            ratingTextView,
            favoriteBtn) = createRefs()

        AsyncImage(
            modifier = Modifier
                .constrainAs(ref = organizationImageView, constrainBlock = {
                    top.linkTo(parent.top)
                })
                .fillMaxWidth()
                .height(180.dp),
            model = ImageRequest.Builder(LocalContext.current)
                .data(entry.photo)
                .build(),
            contentScale = ContentScale.Crop,
            contentDescription = null,
        )

        Text(
            text = entry.name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.constrainAs(
                ref = titleView,
                constrainBlock = {
                    top.linkTo(organizationImageView.bottom, margin = 8.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                    end.linkTo(favoriteBtn.start)
                    width = Dimension.fillToConstraints
                })
        )

        Image(
            painter = painterResource(id = favoriteImage),
            colorFilter = ColorFilter.tint(colorAccentBlue),
            contentDescription = null,
            modifier = Modifier
                .constrainAs(
                    ref = favoriteBtn,
                    constrainBlock = {
                        top.linkTo(titleView.top)
                        bottom.linkTo(titleView.bottom)
                        end.linkTo(parent.end, margin = 16.dp)
                    })
                .clickable {
                    if (entry.isFavorite) viewmodel.deleteFromFavorite(entry.id)
                    else viewmodel.addToFavorite(entry.id)
                }
        )

        Image(
            painter = painterResource(id = R.drawable.ic_star),
            colorFilter = ColorFilter.tint(colorAccentBlue),
            contentDescription = null,
            modifier = Modifier.constrainAs(
                ref = starImageView,
                constrainBlock = {
                    top.linkTo(titleView.bottom, margin = 8.dp)
                    bottom.linkTo(parent.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                })
        )

        Text(
            text = entry.rate.toString(),
            modifier = Modifier.constrainAs(
                ref = ratingTextView,
                constrainBlock = {
                    top.linkTo(starImageView.top)
                    bottom.linkTo(starImageView.bottom)
                    start.linkTo(starImageView.end, margin = 8.dp)
                }
            )
        )

        Text(
            text = entry.cuisines.joinToString(", "),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.constrainAs(
                ref = cuisinesView,
                constrainBlock = {
                    top.linkTo(ratingTextView.top)
                    bottom.linkTo(ratingTextView.bottom)
                    start.linkTo(ratingTextView.end, margin = 16.dp)
                    end.linkTo(parent.end, margin = 8.dp)
                    width = Dimension.fillToConstraints
                })
        )
    }
}